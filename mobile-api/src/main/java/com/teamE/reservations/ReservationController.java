package com.teamE.reservations;

import com.teamE.common.UsersDemandingController;
import com.teamE.rooms.Room;
import com.teamE.rooms.RoomRepository;
import com.teamE.rooms.RoomWithConfigurationProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
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

    @PostMapping
    @ResponseBody
    public Reservation reserve(@RequestBody ReservationPOJO pojo) {
        Reservation reservation = reservationPOJOToReservationTransformer.transform(pojo);
        reservation.setUser(getUser());
        return reservationRepo.save(reservation);
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
}
