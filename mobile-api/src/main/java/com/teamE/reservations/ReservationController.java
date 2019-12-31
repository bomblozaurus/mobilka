package com.teamE.reservations;

import com.teamE.common.UsersDemandingController;
import com.teamE.rooms.Room;
import com.teamE.rooms.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
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
    public Reservation reserve(ReservationPOJO pojo) {
        Reservation reservation = reservationPOJOToReservationTransformer.transform(pojo);
        reservation.setUser(getUser());
        return reservationRepo.save(reservation);
    }

    @GetMapping
    public Page<Reservation> getUserReservations(final Pageable pageable) {
        return reservationRepo.getAllByUserId(getUserId(), pageable);
    }

    @GetMapping("keyholder")
    public Page<Reservation> getKeyHolderReservations(final Pageable pageable) {
        List<Room> keyHolderRooms = roomRepository.getAllByKeyholder(getUser());
        return reservationRepo.getAllByRoomIn(keyHolderRooms, pageable);
    }
}
