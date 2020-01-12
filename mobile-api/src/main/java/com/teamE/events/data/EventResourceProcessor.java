package com.teamE.events.data;

import com.teamE.events.data.EventsRepo;
import com.teamE.events.data.entity.Event;
import com.teamE.fileUpload.controller.FileController;
import com.teamE.rooms.RoomWithConfigurationProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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


@Configuration
public class EventResourceProcessor implements RepresentationModelProcessor<EntityModel<Event>> {

    @Override
    public EntityModel<Event> process(EntityModel<Event> resource) {
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();

        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FileController.class).
                downloadMainImage(Objects.requireNonNull(resource.getContent()).getMainImage(), request)).withRel("mainImage"));

        return resource;
    }

    public EntityModel<Event> process(Event event) {
        return this.process(new EntityModel<>(event));
    }

    public Collection<EntityModel<Event>> process(Collection<Event> events) {
        return events.stream().map(this::process).collect(Collectors.toList());
    }

}












