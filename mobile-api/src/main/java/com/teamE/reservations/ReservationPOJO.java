package com.teamE.reservations;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ReservationPOJO {
    private LocalDateTime dateTime;

    private LocalTime duration;

    private int numberOfPeople;
}
