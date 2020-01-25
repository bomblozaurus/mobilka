package com.teamE.reservations;

import com.teamE.common.UsersDemandingController;
import com.teamE.rooms.Room;
import com.teamE.rooms.RoomRepository;
import com.teamE.rooms.RoomWithConfigurationProjection;
import com.teamE.users.User;
import com.teamE.users.UserToUserTokenInformationTransformer;
import com.teamE.users.UserTokenInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("reservations")
public class ReservationController extends UsersDemandingController {

    private ReservationPOJOToReservationTransformer reservationPOJOToReservationTransformer;
    private UserToUserTokenInformationTransformer userToUserTokenInformationTransformer;
    private ReservationRepo reservationRepo;
    private RoomRepository roomRepository;

    @Autowired
    public ReservationController(ReservationPOJOToReservationTransformer reservationPOJOToReservationTransformer, UserToUserTokenInformationTransformer userToUserTokenInformationTransformer, ReservationRepo reservationRepo, RoomRepository roomRepository) {
        super();
        this.reservationPOJOToReservationTransformer = reservationPOJOToReservationTransformer;
        this.userToUserTokenInformationTransformer = userToUserTokenInformationTransformer;
        this.reservationRepo = reservationRepo;
        this.roomRepository = roomRepository;
    }

    @PostMapping
    @ResponseBody
    public Reservation reserve(@RequestBody ReservationPOJO pojo) {
        Reservation reservation = reservationPOJOToReservationTransformer.transform(pojo);
        if (new ReservationValidator(reservationRepo).validate(reservation)) {
            reservation.setUser(getUser());
            reservation.setCreationDate(LocalDateTime.now());
            return reservationRepo.save(reservation);
        } else
            return reservation;
    }

    @GetMapping
    public Page<SimpleReservationProjection> getUserReservations(final Pageable pageable, @RequestParam final String query) {
        Collection<Room> matchingRooms = getMatchingRooms(query);
        return reservationRepo.getAllByUserIdAndRoomIn(getUserId(), matchingRooms, pageable);
    }

    @GetMapping("owner")
    public ResponseEntity<UserTokenInformation> getReservationOwner(@RequestParam final long reservationId) {
        if (!isUserKeyholder()) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be a keyholder to use this method!");
        }

        Reservation reservation = reservationRepo.getOne(reservationId);
        User owner = reservation.getUser();
        UserTokenInformation ownerInformation = userToUserTokenInformationTransformer.transform(owner);
        return ResponseEntity.status(HttpStatus.OK).body(ownerInformation);
    }

    @GetMapping("keyholder")
    public Page<SimpleReservationProjection> getKeyHolderReservations(final Pageable pageable, @RequestParam final String query) {
        List<Room> keyHolderRooms = roomRepository.getAllByKeyholder(getUser());
        return reservationRepo.getAllByRoomIn(keyHolderRooms, pageable);
    }

    @PutMapping("switch")
    public ResponseEntity<String> switchReservationConfirmed(@RequestParam final long reservationId) {
        Reservation reservation = reservationRepo.getOne(reservationId);

        if (!Objects.equals(getUser(), reservation.getRoom().getKeyholder())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("To modify the room you must be its keyholder!");
        }

        reservation.switchAccepted();
        reservation = reservationRepo.save(reservation);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Boolean.valueOf(reservation.isAccepted()).toString());
    }


    private Collection<Room> getMatchingRooms(@RequestParam String query) {
        Collection<RoomWithConfigurationProjection> matchingRoomProjections = roomRepository.getAllByDsNumberAndQuery(getUserStudentHouseId(), query, null).getContent();
        Collection<Long> matchingRoomIds = matchingRoomProjections.stream().map(RoomWithConfigurationProjection::getId).collect(Collectors.toList());
        return roomRepository.findAllById(matchingRoomIds);
    }

    @GetMapping("freeForRoom")
    public Map<LocalTime, List<Duration>> getFreeHoursForRoom(@RequestParam long roomId, @RequestParam String date) {
        HashMap<LocalTime, List<Duration>> map = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ld = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = LocalDateTime.of(ld, LocalDateTime.now().toLocalTime());
        List<Reservation> list = reservationRepo.getAllByRoomIdAndCalendarDate(roomId, dateTime.getYear(), dateTime.getMonth().getValue(), dateTime.getDayOfMonth());
        Room room = roomRepository.getOne(roomId);
        int countOfAvailableIntervals = (int) (((room.getConfiguration().getOpenTo().toSecondOfDay() - room.getConfiguration().getOpenFrom().toSecondOfDay()) / room.getConfiguration().getRentInterval().getSeconds()));
        for (int i = 0; i < countOfAvailableIntervals; i++) {
            ArrayList<Duration> durations = new ArrayList<>();
            boolean[] availabilityFutureHours = new boolean[countOfAvailableIntervals - i];
            LocalTime current = room.getConfiguration().getOpenFrom().plus(room.getConfiguration().getRentInterval().multipliedBy(i));
            boolean flag = true;
            for (int j = 0; j < countOfAvailableIntervals - i; j++) {
                for (Reservation reservation : list) {
                    if (current.plus(room.getConfiguration().getRentInterval().multipliedBy(j)).equals((reservation.getDateTime().toLocalTime()))) {
                        flag = false;
                        break;
                    }
                    if (current.plus(room.getConfiguration().getRentInterval().multipliedBy(j)).isAfter(reservation.getDateTime().toLocalTime()) && current.plus(room.getConfiguration().getRentInterval().multipliedBy(j)).isBefore(reservation.getDateTime().plus(reservation.getDuration()).toLocalTime())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    availabilityFutureHours[j] = flag;
                } else
                    break;

            }
            for (int j = 0; j < availabilityFutureHours.length; j++) {
                if (availabilityFutureHours[j])
                    durations.add(room.getConfiguration().getRentInterval().multipliedBy(j + 1));

                else
                    break;
            }
              if (durations.size() > 0) {
                if (ld.isAfter(LocalDate.now())) {
                    map.put(current, durations);
                    break;
                }
                if (ld.isEqual(LocalDate.now()) && current.isAfter(LocalTime.now())) {
                    map.put(current, durations);
                }
            }
        }
        return new TreeMap<>(map);
    }

    @DeleteMapping
    Boolean deleteReservation(@RequestParam long id) {
        Optional<Reservation> optionalReservation = reservationRepo.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            if (reservation.getDateTime().isAfter(LocalDateTime.now()) && (reservation.getUser().equals(getUser()) || reservation.getRoom().getKeyholder().equals(getUser()))) {
                reservationRepo.deleteById(id);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
