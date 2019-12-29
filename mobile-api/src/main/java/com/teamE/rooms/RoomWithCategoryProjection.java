package com.teamE.rooms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalTime;

@Projection(name = "category", types = Room.class)
public interface RoomWithCategoryProjection {
    long getId();

    int getDsNumber();

    String getName();

    String getDescription();

    @Value("#{target.getConfiguration().getOpenFrom()}")
    LocalTime getOpenFrom();

    @Value("#{target.getConfiguration().getOpenTo()}")
    LocalTime getOpenTo();

    @Value("#{target.getConfiguration().getRentInterval()}")
    LocalTime getRentInterval();

    @Value("#{target.getConfiguration().getPricePerInterval()}")
    BigDecimal getPricePerInterval();
}
