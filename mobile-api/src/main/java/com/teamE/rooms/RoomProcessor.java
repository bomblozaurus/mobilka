package com.teamE.rooms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class RoomProcessor implements RepresentationModelProcessor<EntityModel<Room>> {

    @Autowired
    private RepositoryEntityLinks repositoryEntityLinks;

    @Override
    public EntityModel<Room> process(EntityModel<Room> model) {
        try {
            model.add(repositoryEntityLinks.linkToItemResource(RoomConfigurationRepository.class, model.getContent().getConfiguration().getId().toString()));
        } catch (NullPointerException npe) {
            log.error("Room without configuration is returned by" + this.getClass().getName());
        }
        return model;
    }

    public EntityModel<Room> process(Room room) {
        return this.process(new EntityModel<>(room));
    }

    public Collection<EntityModel<Room>> process(Collection<Room> rooms) {
        return rooms.stream().map(this::process).collect(Collectors.toList());
    }

}
