package com.teamE.rooms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

@Projection(name = "configuration", types = Room.class)
public interface RoomWithConfigurationProjection {
    long getId();

    int getDsNumber();

    String getName();

    Long getMainImage();

    String getDescription();

    @Value("#{target.getConfiguration().getOpenFrom()}")
    LocalTime getOpenFrom();

    @Value("#{target.getConfiguration().getOpenTo()}")
    LocalTime getOpenTo();

    @Value("#{target.getConfiguration().getRentInterval()}")
    Duration getRentInterval();

    @Value("#{target.getConfiguration().getPricePerInterval()}")
    BigDecimal getPricePerInterval();
}
