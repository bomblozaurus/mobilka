package com.teamE.ads.controllers;

import com.teamE.ads.data.entity.Ad;
import com.teamE.ads.data.entity.AdPOJO;
import com.teamE.ads.data.entity.AdPOJOToAdTransformer;
import com.teamE.ads.data.entity.AdValidator;
import com.teamE.ads.managers.AdManager;
import com.teamE.common.ValidationHandler;
import com.teamE.commonAddsEvents.Address;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.commonAddsEvents.converters.ScopeConverter;
import com.teamE.commonAddsEvents.converters.StudentHouseConverter;
import com.teamE.imageDestinations.Destination;
import com.teamE.imageDestinations.ImageDestination;
import com.teamE.imageDestinations.ImageDestinationRepo;
import com.teamE.users.StudentHouse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ads")
public class AdsController {
    private AdManager adManager;
    private ImageDestinationRepo imageDestinationRepo;
    private AdValidator adValidator;
    private AdPOJOToAdTransformer adPOJOToAdTransformer;

    @Autowired
    public AdsController(AdManager adManager, ImageDestinationRepo imageDestinationRepo, AdValidator adValidator, AdPOJOToAdTransformer adPOJOToAdTransformer) {
        this.adManager = adManager;
        this.imageDestinationRepo = imageDestinationRepo;
        this.adValidator = adValidator;
        this.adPOJOToAdTransformer = adPOJOToAdTransformer;
    }

    @GetMapping("/all")
    public Iterable<Ad> getAll() {
        return adManager.findAll();
    }

    @GetMapping
    public Optional<Ad> getById(@RequestParam Long index) {
        return adManager.findById(index);
    }

    @GetMapping("/scope")
    public Iterable<Ad> getByScope(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {
        if (scope.equals(Scope.DORMITORY)) {
            return adManager.findByScopeAndStudentHouse(scope, studentHouse);
        } else {
            return adManager.findByScope(scope);
        }
    }
    @GetMapping("/scopeOrderPriceDesc")
    public Iterable<Ad> getByScopeOrderByDateDesc(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {
        if (scope.equals(Scope.DORMITORY)) {
            return adManager.findByScopeAndStudentHouseOrderByPriceDesc(scope, studentHouse);
        } else {
            return adManager.findByScopeOrderByPriceDesc(scope);
        }
    }

    @GetMapping("/address")
    public Address getAddress() {
        return new Address();
    }

    @PostMapping("/address")
    public Address addAddress(@RequestBody Address address) {
        return adManager.save(address);
    }

    @PostMapping
    public Ad addAd(@RequestBody @Validated AdPOJO adPojo) {
        Ad ad= adPOJOToAdTransformer.transform(adPojo);
        Ad savedAd = adManager.save(ad);

        Long mainImage = ad.getMainImage();
        List<Long> additionalImages = adPojo.getAdditionalImages();

        Optional<ImageDestination> temp = imageDestinationRepo.findById(mainImage);
        if (temp.isPresent()) {
            ImageDestination imageDestination = temp.get();
            imageDestination.setIdDestination(savedAd.getId());
            imageDestination.setDestination(Destination.AD);
            imageDestinationRepo.save(imageDestination);
        }

        if (CollectionUtils.isNotEmpty(additionalImages)) {
            for (Long image : additionalImages) {
                temp = imageDestinationRepo.findById(image);
                if (temp.isPresent()) {
                    ImageDestination imageDestination = temp.get();
                    imageDestination.setIdDestination(savedAd.getId());
                    imageDestination.setDestination(Destination.AD);
                    imageDestinationRepo.save(imageDestination);
                }
            }
        }
        return savedAd;
    }

    @DeleteMapping
    public void deleteAd(@RequestParam Long index) {
        adManager.deleteById(index);
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(Scope.class, new ScopeConverter());
        webdataBinder.registerCustomEditor(StudentHouse.class, new StudentHouseConverter());
        webdataBinder.setValidator(adValidator);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return ValidationHandler.handleValidationExceptions(ex);
    }
}
