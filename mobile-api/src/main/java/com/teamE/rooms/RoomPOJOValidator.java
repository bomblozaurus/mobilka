package com.teamE.rooms;

import com.teamE.events.data.entity.Event;
import com.teamE.imageDestinations.ImageDestinationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Configuration
public class RoomPOJOValidator implements Validator {
    private ImageDestinationRepo imageDestinationRepo;

    @Autowired
    public RoomPOJOValidator(ImageDestinationRepo imageDestinationRepo) {
        this.imageDestinationRepo = imageDestinationRepo;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RoomPOJO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RoomPOJO room = (RoomPOJO) o;

        if (room.getMainImage() != null) {
            if (!imageDestinationRepo.existsByIdAndIdDestination(room.getMainImage(), -1L)) {
                errors.rejectValue("mainImage", "This image not exists or is already used");
            }
        }

    }
}
