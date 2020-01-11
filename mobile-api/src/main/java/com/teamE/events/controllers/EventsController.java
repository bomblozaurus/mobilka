package com.teamE.events.controllers;

import com.teamE.ads.data.entity.Ad;
import com.teamE.common.UsersDemandingController;
import com.teamE.common.ValidationHandler;
import com.teamE.commonAddsEvents.Address;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.commonAddsEvents.converters.ScopeConverter;
import com.teamE.commonAddsEvents.converters.StudentHouseConverter;
import com.teamE.events.data.EventsRepo;
import com.teamE.events.data.entity.Event;
import com.teamE.events.data.entity.EventValidator;
import com.teamE.events.manager.EventManager;
import com.teamE.imageDestinations.Destination;
import com.teamE.imageDestinations.ImageDestination;
import com.teamE.imageDestinations.ImageDestinationRepo;
import com.teamE.users.StudentHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController()
@RequestMapping("events")
public class EventsController extends UsersDemandingController {

    private EventManager eventManager;
    private EventsRepo eventsRepo;
    private ImageDestinationRepo imageDestinationRepo;
    private EventValidator eventValidator;

    @Autowired
    public EventsController(EventManager eventManager, EventsRepo eventsRepo, ImageDestinationRepo imageDestinationRepo, EventValidator eventValidator) {
        this.eventManager = eventManager;
        this.eventsRepo = eventsRepo;
        this.imageDestinationRepo = imageDestinationRepo;
        this.eventValidator = eventValidator;
    }

    @GetMapping
    public Page<Event> getAllEventsAvailableForUser(final Pageable pageable) {
        //TODO sprawdzac rolę użytokownika i zwracac tylko wydarzenia z odpowiednich scope'ów
        return eventsRepo.findAll(pageable);
    }

    public Page<Event> findForUser(final Pageable pageable, final String query) {
        //FIXME dodać scope
        return eventsRepo.findAllByScopeAndStudentHouseAndQuery(null, getUserStudentHouse(), query, pageable);
    }

    @GetMapping("/scope")
    public Iterable<Event> getByScope(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {
        if (scope.equals(Scope.DORMITORY)) {
            return eventManager.findByScopeAndStudentHouse(scope, studentHouse);
        } else {
            return eventManager.findByScope(scope);
        }
    }

    @PostMapping
    public Event addEvent(@RequestBody @Validated Event event) {
        Event savedEvent = eventsRepo.save(event);

        Long mainImage = savedEvent.getMainImage();

        Optional<ImageDestination> temp = imageDestinationRepo.findById(mainImage);
        if (temp.isPresent()) {
            temp.get().setIdDestination(savedEvent.getId());
            temp.get().setDestination(Destination.EVENT);
            imageDestinationRepo.save(temp.get());
        }
        return savedEvent;
    }

    @GetMapping("/scopeOrderDate")
    public Iterable<Event> getByScopeOrderByDateDesc(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {
        if (scope.equals(Scope.DORMITORY)) {
            return eventManager.findByScopeAndStudentHouseOrderByDateDesc(scope, studentHouse);
        } else {
            return eventManager.findByScopeOrderByDateDesc(scope);
        }
    }

    @GetMapping("/address")
    public Address getAddress() {
        return new Address();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return ValidationHandler.handleValidationExceptions(ex);
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(Scope.class, new ScopeConverter());
        webdataBinder.registerCustomEditor(StudentHouse.class, new StudentHouseConverter());
        webdataBinder.setValidator(eventValidator);
    }
}
