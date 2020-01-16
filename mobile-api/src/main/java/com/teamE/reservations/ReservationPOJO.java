package com.teamE.reservations;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class ReservationPOJO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    private Duration duration;

    private int numberOfPeople;

    private int roomId;

}
