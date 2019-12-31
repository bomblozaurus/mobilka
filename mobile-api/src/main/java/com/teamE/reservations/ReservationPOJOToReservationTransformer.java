package com.teamE.reservations;

import com.teamE.common.Transformer;
import org.springframework.stereotype.Component;

@Component
public class ReservationPOJOToReservationTransformer implements Transformer<ReservationPOJO, Reservation> {
    @Override
    public Reservation transform(ReservationPOJO pojo) {
        return Reservation.builder()
                .dateTime(pojo.getDateTime())
                .duration(pojo.getDuration())
                .numberOfPeople(pojo.getNumberOfPeople())
                .build();
    }
}
