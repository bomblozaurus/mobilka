package com.teamE.events.data.entity;

import com.teamE.imageDestinations.ImageDestination;
import com.teamE.imageDestinations.ImageDestinationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Configuration
public class EventValidator implements Validator {
    private ImageDestinationRepo imageDestinationRepo;

    @Autowired
    public EventValidator(ImageDestinationRepo imageDestinationRepo) {
        this.imageDestinationRepo = imageDestinationRepo;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Event.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        Event event = (Event) o;

        if (event.getMainImage() != null) {
            if (!imageDestinationRepo.existsByIdAndIdDestination(event.getMainImage(), -1L)) {
                errors.rejectValue("mainImage", "This image not exists or is already used");
            }
        }
    }
}
