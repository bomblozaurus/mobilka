package com.teamE.reservations;

import com.teamE.common.UsersDemandingController;
import com.teamE.rooms.Room;
import com.teamE.rooms.RoomRepository;
import com.teamE.rooms.RoomWithConfigurationProjection;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    private ReservationRepo reservationRepo;
    private RoomRepository roomRepository;


    @Autowired
    public ReservationController(ReservationPOJOToReservationTransformer reservationPOJOToReservationTransformer, ReservationRepo reservationRepo, RoomRepository roomRepository) {
        super();
        this.reservationPOJOToReservationTransformer = reservationPOJOToReservationTransformer;
        this.reservationRepo = reservationRepo;
        this.roomRepository = roomRepository;
    }

    @PostMapping("reservation")
    @ResponseBody
    public Reservation reserve(@RequestBody ReservationPOJO pojo) {
        Reservation reservation = reservationPOJOToReservationTransformer.transform(pojo);
        if(new ReservationValidator(reservationRepo).validate(reservation)) {
            reservation.setUser(getUser());
            return reservationRepo.save(reservation);
        }
        else
            return reservation;
        }

    @GetMapping
    public Page<SimpleReservationProjection> getUserReservations(final Pageable pageable, @RequestParam final String query) {
        Collection<Room> matchingRooms = getMatchingRooms(query);
        return reservationRepo.getAllByUserIdAndRoomIn(getUserId(), matchingRooms, pageable);
    }

    @GetMapping("keyholder")
    public Page<SimpleReservationProjection> getKeyHolderReservations(final Pageable pageable, @RequestParam final String query) {
        List<Room> keyHolderRooms = roomRepository.getAllByKeyholder(getUser());
        return reservationRepo.getAllByRoomIn(keyHolderRooms, pageable);
    }


    private Collection<Room> getMatchingRooms(@RequestParam String query) {
        Collection<RoomWithConfigurationProjection> matchingRoomProjections = roomRepository.getAllByDsNumberAndQuery(getUserStudentHouseId(), query, null).getContent();
        Collection<Long> matchingRoomIds = matchingRoomProjections.stream().map(RoomWithConfigurationProjection::getId).collect(Collectors.toList());
        return roomRepository.findAllById(matchingRoomIds);
    }

    @GetMapping("freeForRoom")
    public Map<LocalTime, List<Duration>>getFreeHoursForRoom(@RequestParam long roomId, @RequestParam String date){
        HashMap<LocalTime,List<Duration>> map = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Zostawiłem godziny by łatwiej było przekazywac z apki bo chyba w takim formacie tam jest zapisane ale mozna je smialo wypierdolic
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        List <Reservation> list = reservationRepo.getAllByRoomIdAndCalendarDate(roomId,dateTime.getYear(),dateTime.getMonth().getValue(),dateTime.getDayOfMonth());
        Room room = roomRepository.getOne(roomId);
        int countOfAvailableIntervals = (int) (((room.getConfiguration().getOpenTo().toSecondOfDay() - room.getConfiguration().getOpenFrom().toSecondOfDay())/room.getConfiguration().getRentInterval().getSeconds()));
        for(int i=0;i<countOfAvailableIntervals;i++) {
            ArrayList<Duration> durations = new ArrayList<>();
            boolean[] availabilityFutureHours = new boolean[countOfAvailableIntervals-i];
            LocalTime current = room.getConfiguration().getOpenFrom().plus(room.getConfiguration().getRentInterval().multipliedBy(i));
            boolean flag = true;
            for(int j=0;j<countOfAvailableIntervals-i;j++){
               for(int k=0;k<list.size();k++){
                   if(current.plus(room.getConfiguration().getRentInterval().multipliedBy(j)).equals((list.get(k).getDateTime().toLocalTime()))) {
                       flag=false;
                       break;
                   }
                   if(current.plus(room.getConfiguration().getRentInterval().multipliedBy(j)).isAfter(list.get(k).getDateTime().toLocalTime()) && current.plus(room.getConfiguration().getRentInterval().multipliedBy(j)).isBefore(list.get(k).getDateTime().plus(list.get(k).getDuration()).toLocalTime())) {
                        flag=false;
                        break;
                   }
               }
                if(flag) {
                    availabilityFutureHours[j]= flag;
                }
                else
                    break;
               }
            for(int j=0;j<availabilityFutureHours.length;j++)
            {
                if(availabilityFutureHours[j])
                    durations.add(room.getConfiguration().getRentInterval().multipliedBy(j));
                else
                    break;
            }
            map.put(current,durations);
        }
        return new TreeMap<>(map);
    }
}
