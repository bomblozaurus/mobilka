package com.teamE.events.data;

import com.teamE.events.data.EventsRepo;
import com.teamE.events.data.entity.Event;
import com.teamE.fileUpload.controller.FileController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@Configuration
public class EventResourceProcessor {
    EventsRepo eventsRepo;

    @Autowired
    public EventResourceProcessor(EventsRepo eventsRepo) {
        this.eventsRepo = eventsRepo;
    }

    @Bean
    public RepresentationModelProcessor<EntityModel<Event>> productProcessor() {

        return new RepresentationModelProcessor<EntityModel<Event>>() {
            @Override
            public EntityModel<Event> process(EntityModel<Event> resource) {
                HttpServletRequest request =
                        ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                                .getRequest();

                resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FileController.class).
                        downloadMainImage(Objects.requireNonNull(resource.getContent()).getId(), request)).withRel("mainImage"));

                return resource;
            }
        };

    }
}
