package com.teamE.events.controllers;

import com.teamE.ads.data.entity.Ad;
import com.teamE.common.UsersDemandingController;
import com.teamE.common.ValidationHandler;
import com.teamE.commonAddsEvents.Address;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.commonAddsEvents.converters.ScopeConverter;
import com.teamE.commonAddsEvents.converters.StudentHouseConverter;
import com.teamE.events.data.EventResourceProcessor;
import com.teamE.events.data.EventsRepo;
import com.teamE.events.data.entity.Event;
import com.teamE.events.data.entity.EventValidator;
import com.teamE.events.manager.EventManager;
import com.teamE.imageDestinations.Destination;
import com.teamE.imageDestinations.ImageDestination;
import com.teamE.imageDestinations.ImageDestinationRepo;
import com.teamE.rooms.RoomWithConfigurationProjection;
import com.teamE.users.StudentHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController()
@RequestMapping("events")
public class EventsController extends UsersDemandingController {

    private EventManager eventManager;
    private EventsRepo eventsRepo;
    private ImageDestinationRepo imageDestinationRepo;
    private EventValidator eventValidator;
    private EventResourceProcessor eventResourceProcessor;

    @Autowired
    public EventsController(EventManager eventManager, EventsRepo eventsRepo, ImageDestinationRepo imageDestinationRepo, EventValidator eventValidator, EventResourceProcessor eventResourceProcessor) {
        this.eventManager = eventManager;
        this.eventsRepo = eventsRepo;
        this.imageDestinationRepo = imageDestinationRepo;
        this.eventValidator = eventValidator;
        this.eventResourceProcessor = eventResourceProcessor;
    }

    @GetMapping
    public Page<EntityModel<Event>> getAllEventsAvailableForUser(final Pageable pageable) {
        //TODO sprawdzac rolę użytokownika i zwracac tylko wydarzenia z odpowiednich scope'ów
        Page<Event> page = eventsRepo.findAll(pageable);
        return page.map(e -> eventResourceProcessor.process(e));
    }

    public Page<EntityModel<Event>> findForUser(final Pageable pageable, final String query) {
        //FIXME dodać scope
        Page<Event> page = eventsRepo.findAllByScopeAndStudentHouseAndQuery(null, getUserStudentHouse(), query, pageable);
        return page.map(e -> eventResourceProcessor.process(e));
    }

    @GetMapping("/scope")
    public Iterable<EntityModel<Event>> getByScope(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {
        Iterable<Event> events;
        if (scope.equals(Scope.DORMITORY)) {
            events = eventManager.findByScopeAndStudentHouse(scope, studentHouse);
        } else {
            events = eventManager.findByScope(scope);
        }
        List<EntityModel<Event>> toReturn = new ArrayList<>();
        for (Event e :
                events) {
            toReturn.add(eventResourceProcessor.process(e));
        }
        return toReturn;
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
    public Iterable<EntityModel<Event>> getByScopeOrderByDateDesc(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {
        Iterable<Event> events;
        if (scope.equals(Scope.DORMITORY)) {
            events = eventManager.findByScopeAndStudentHouseOrderByDateDesc(scope, studentHouse);
        } else {
            events = eventManager.findByScopeOrderByDateDesc(scope);
        }
        List<EntityModel<Event>> toReturn = new ArrayList<>();
        for (Event e :
                events) {
            toReturn.add(eventResourceProcessor.process(e));
        }
        return toReturn;
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
