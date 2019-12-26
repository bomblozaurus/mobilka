package com.teamE.events.controllers;


import com.teamE.commonAddsEvents.converters.ScopeConverter;
import com.teamE.commonAddsEvents.converters.StudentHouseConverter;
import com.teamE.commonAddsEvents.Address;
import com.teamE.events.data.entity.Event;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.events.manager.EventManager;
import com.teamE.users.StudentHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
//@RequestMapping("/events")
public class EventsController {

    private EventManager eventManager;

    @Autowired
    public EventsController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @GetMapping("/all")
    public Iterable<Event> getAll() {
        return eventManager.findAll();
    }

    @GetMapping
    public Optional<Event> getById(@RequestParam Long index) {
        return eventManager.findById(index);
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

    @PostMapping
    public Event addEvent(@RequestBody Event event) {
        return eventManager.save(event);
    }

    @DeleteMapping
    public void deleteEvent(@RequestParam Long index) {
        eventManager.deleteById(index);
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(Scope.class, new ScopeConverter());
        webdataBinder.registerCustomEditor(StudentHouse.class, new StudentHouseConverter());
    }
}
