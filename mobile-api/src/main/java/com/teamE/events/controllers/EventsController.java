package com.teamE.events.controllers;


import com.teamE.events.data.entity.Address;
import com.teamE.events.data.entity.Event;
import com.teamE.events.data.entity.Scope;
import com.teamE.events.manager.EventManager;
import com.teamE.users.StudentHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/events")
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

/*    @GetMapping
    public Optional<Event> getByScope(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {

    }*/

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
}
