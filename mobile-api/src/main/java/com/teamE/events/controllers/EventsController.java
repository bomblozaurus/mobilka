package com.teamE.events.controllers;


import com.teamE.commonAddsEvents.converters.ScopeConverter;
import com.teamE.commonAddsEvents.converters.StudentHouseConverter;
import com.teamE.commonAddsEvents.Address;
import com.teamE.events.data.EventsRepo;
import com.teamE.events.data.entity.Event;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.events.manager.EventManager;
import com.teamE.users.StudentHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventsController {

    private EventManager eventManager;
    private EventsRepo eventsRepo;

    @Autowired
    public EventsController(EventManager eventManager, EventsRepo eventsRepo) {
        this.eventManager = eventManager;
        this.eventsRepo = eventsRepo;
    }

    @GetMapping
    public Page<Event> getAllEventsAvailableForUser(final Pageable pageable){
        //TODO sprawdzac rolę użytokownika i zwracac tylko wydarzenia z odpowiednich scope'ów
        return eventsRepo.findAll(pageable);
    }

   @GetMapping("/scope")
    public Iterable<Event> getByScope(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {
        if (scope.equals(Scope.DORMITORY)) {
            return eventManager.findByScopeAndStudentHouse(scope, studentHouse);
        } else {
            return eventManager.findByScope(scope);
        }
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

    @PostMapping("/address")
    public Address addAddress(@RequestBody Address address) {
        return eventManager.save(address);
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(Scope.class, new ScopeConverter());
        webdataBinder.registerCustomEditor(StudentHouse.class, new StudentHouseConverter());
    }
}
