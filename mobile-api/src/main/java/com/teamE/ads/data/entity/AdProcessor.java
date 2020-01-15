package com.teamE.ads.data.entity;

import com.teamE.fileUpload.controller.FileController;
import com.teamE.imageDestinations.Destination;
import com.teamE.imageDestinations.ImageDestination;
import com.teamE.imageDestinations.ImageDestinationRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
public class AdProcessor implements RepresentationModelProcessor<EntityModel<Ad>> {

    private ImageDestinationRepo imageDestinationRepo;

    @Autowired
    public AdProcessor(ImageDestinationRepo imageDestinationRepo) {
        this.imageDestinationRepo = imageDestinationRepo;
    }

    @Override
    public EntityModel<Ad> process(EntityModel<Ad> model) {

        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();

        Long mainImageId = Objects.requireNonNull(model.getContent()).getMainImage();

        if (mainImageId != null) {
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FileController.class).
                    downloadMainImage(mainImageId, request)).withRel("mainImage"));

            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FileController.class).
                    downloadMainImage(mainImageId, request)).withRel("additionalImages"));
        } else {
            model.add(new Link("https://archive.org/download/no-photo-available/no-photo-available.png").withRel("mainImage"));
            model.add(new Link("https://archive.org/download/no-photo-available/no-photo-available.png").withRel("additionalImages"));
        }

        for (ImageDestination image :
                imageDestinationRepo.findAllByIdDestinationAndDestination(Objects.requireNonNull(model.getContent()).getId(), Destination.AD)) {
            if (!image.getId().equals(mainImageId)) {
                model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FileController.class).
                        downloadAdditionalImage(image.getId(), request)).withRel("additionalImages"));
            }
        }

        return model;
    }

    public EntityModel<Ad> process(Ad ad) {
        return this.process(new EntityModel<>(ad));
    }

    public Collection<EntityModel<Ad>> process(Collection<Ad> ads) {
        return ads.stream().map(this::process).collect(Collectors.toList());
    }

}
