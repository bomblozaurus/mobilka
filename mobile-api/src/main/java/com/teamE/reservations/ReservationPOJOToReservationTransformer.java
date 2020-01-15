package com.teamE.reservations;

import com.teamE.common.Transformer;
import com.teamE.rooms.Room;
import com.teamE.rooms.RoomRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ReservationPOJOToReservationTransformer implements Transformer<ReservationPOJO, Reservation> {

    private RoomRepository roomRepository;

    public ReservationPOJOToReservationTransformer(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Reservation transform(ReservationPOJO pojo) {
        return Reservation.builder()
                .dateTime(pojo.getDateTime())
                .duration(pojo.getDuration())
                .numberOfPeople(pojo.getNumberOfPeople())
                .room(getRoom(pojo.getRoomId()))
                .build();
    }

    private Room getRoom(long roomId) {
        System.out.println(roomId);
        return roomRepository.findById(roomId).orElseThrow(ResourceNotFoundException::new);
    }
}
