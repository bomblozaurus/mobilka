package com.teamE.rooms;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

@Value
public class RoomWithConfigurationDto {
    long id;
    int dsNumber;
    String name;
    Long mainImage;
    String description;
    LocalTime openFrom;
    LocalTime openTo;
    Duration getRentInterval;
    BigDecimal pricePerInterval;
}
