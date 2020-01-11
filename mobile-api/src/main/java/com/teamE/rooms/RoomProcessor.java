package com.teamE.rooms;

import com.teamE.fileUpload.controller.FileController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class RoomProcessor implements RepresentationModelProcessor<EntityModel<Room>> {

    @Autowired
    private RepositoryEntityLinks repositoryEntityLinks;

    @Override
    public EntityModel<Room> process(EntityModel<Room> model) {

        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FileController.class).
                downloadMainImage(Objects.requireNonNull(model.getContent()).getMainImage(), request)).withRel("mainImage"));

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
