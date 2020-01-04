package com.teamE.reservations;

import com.teamE.rooms.Room;
import com.teamE.rooms.RoomConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;



public class ReservationValidator {


    @Autowired
    private  ReservationRepo reservationRepo;


    public ReservationValidator(ReservationRepo reservationRepo){this.reservationRepo = reservationRepo;}


    public boolean validate(Reservation reservation){
        Room room = reservation.getRoom();
        RoomConfiguration config = room.getConfiguration();
        List<Reservation> list  = reservationRepo.getAllByRoomIdAndDateTimeBetween(room.getId(),reservation.getDateTime(),reservation.getDateTime().plus(reservation.getDuration()));
        if(reservation.getDuration().getSeconds() > 86400 - reservation.getDateTime().toLocalTime().toSecondOfDay())
            return false;

        if(reservation.getDateTime().toLocalTime().isBefore (config.getOpenFrom()) || reservation.getDateTime().plus(config.getRentInterval()).toLocalTime().isAfter(config.getOpenTo()))
            return false;

        if(list.size()==0)
            return true;

        return false;



    }
}
