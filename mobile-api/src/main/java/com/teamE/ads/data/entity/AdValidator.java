package com.teamE.ads.data.entity;

import com.teamE.imageDestinations.ImageDestination;
import com.teamE.imageDestinations.ImageDestinationRepo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Set;

@Configuration
public class AdValidator implements Validator {
    private ImageDestinationRepo imageDestinationRepo;

    @Autowired
    public AdValidator(ImageDestinationRepo imageDestinationRepo) {
        this.imageDestinationRepo = imageDestinationRepo;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Ad.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        ValidationUtils.rejectIfEmpty(errors, "scope", "scope.empty");
        Ad ad = (Ad) o;

        if (!imageDestinationRepo.existsByIdAndIdDestination(ad.getMainImage(), -1L)) {
            ValidationUtils.rejectIfEmpty(errors, "mainImage", "This image not exists or is already used");
        }

        Set<Long> additionalImages = ad.getAdditionalImages();
        if (additionalImages != null) {
            if (CollectionUtils.isNotEmpty(additionalImages)) {
                for (Long image : additionalImages) {
                    if (imageDestinationRepo.findById(image).isEmpty()) {
                        errors.rejectValue("additionalImages", "additionalImages not exists");
                    }
                }
            }
        }
    }
}
