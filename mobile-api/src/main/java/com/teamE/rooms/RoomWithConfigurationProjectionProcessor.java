package com.teamE.rooms;

import com.teamE.fileUpload.controller.FileController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class RoomWithConfigurationProjectionProcessor implements RepresentationModelProcessor<EntityModel<RoomWithConfigurationDto>> {

    @Autowired
    private RepositoryEntityLinks repositoryEntityLinks;

    @Override
    public EntityModel<RoomWithConfigurationDto> process(EntityModel<RoomWithConfigurationDto> model) {

        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();

        Long mainImageId = Objects.requireNonNull(model.getContent()).getMainImage();
        if (mainImageId != null) {
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FileController.class).
                    downloadMainImage(mainImageId, request)).withRel("mainImage"));
        } else {
            model.add(new Link("https://archive.org/download/no-photo-available/no-photo-available.png").withRel("mainImage"));
        }

        return model;
    }

    public EntityModel<RoomWithConfigurationDto> process(RoomWithConfigurationDto room) {
        return this.process(new EntityModel<>(room));
    }

    public Collection<EntityModel<RoomWithConfigurationDto>> process(Collection<RoomWithConfigurationDto> rooms) {
        return rooms.stream().map(this::process).collect(Collectors.toList());
    }

}
