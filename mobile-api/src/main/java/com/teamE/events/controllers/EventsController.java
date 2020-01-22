package com.teamE.events.controllers;

import com.teamE.ads.data.entity.Ad;
import com.teamE.common.UsersDemandingController;
import com.teamE.common.ValidationHandler;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.commonAddsEvents.converters.ScopeConverter;
import com.teamE.commonAddsEvents.converters.StudentHouseConverter;
import com.teamE.events.EventSearcher;
import com.teamE.events.data.EventResourceProcessor;
import com.teamE.events.data.EventsRepo;
import com.teamE.events.data.entity.Event;
import com.teamE.events.data.entity.EventValidator;
import com.teamE.imageDestinations.Destination;
import com.teamE.imageDestinations.ImageDestination;
import com.teamE.imageDestinations.ImageDestinationRepo;
import com.teamE.users.StudentHouse;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController()
@RequestMapping("events")
public class EventsController extends UsersDemandingController {


    private EventsRepo eventsRepo;
    private ImageDestinationRepo imageDestinationRepo;
    private EventValidator eventValidator;
    private EventResourceProcessor eventResourceProcessor;
    private EventSearcher eventSearcher;

    @Autowired
    public EventsController(EventsRepo eventsRepo, ImageDestinationRepo imageDestinationRepo, EventValidator eventValidator, EventResourceProcessor eventResourceProcessor, EventSearcher eventSearcher) {
        super();
        this.eventsRepo = eventsRepo;
        this.imageDestinationRepo = imageDestinationRepo;
        this.eventValidator = eventValidator;
        this.eventResourceProcessor = eventResourceProcessor;
        this.eventSearcher = eventSearcher;
    }

    @GetMapping
    public Page<EntityModel<Event>> getAllEventsAvailableForUser(final Pageable pageable) {
        //TODO sprawdzac rolę użytokownika i zwracac tylko wydarzenia z odpowiednich scope'ów
        Page<Event> page = eventsRepo.findAll(pageable);
        return page.map(e -> eventResourceProcessor.process(e));
    }


    public Page<EntityModel<Event>> findForUser(final Pageable pageable, final String query){
     /*   Scope scope = getUserScope();
        StudentHouse studentHouse;
        if (scope != Scope.DORMITORY) {
            studentHouse = null;
        } else {
            studentHouse = getUserStudentHouse();
        }*/
       /* Page<Event> page = eventsRepo.findAllByScopeAndStudentHouseAndQuery(getUserScope(), getUserStudentHouse(), query, pageable);*/
        Page<Event> page = eventsRepo.search(getUserScope(), getUserStudentHouse(), query, pageable);
        return page.map(e -> eventResourceProcessor.process(e));
    }

    @PostMapping
    public Event addEvent(@RequestBody @Validated Event event) {
        Event savedEvent = eventsRepo.save(event);

        Long mainImage = savedEvent.getMainImage();

        if (mainImage != null) {
            Optional<ImageDestination> temp = imageDestinationRepo.findById(mainImage);
            if (temp.isPresent()) {
                temp.get().setIdDestination(savedEvent.getId());
                temp.get().setDestination(Destination.EVENT);
                imageDestinationRepo.save(temp.get());
            }
        }

        return savedEvent;
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
