package com.teamE.events.manager;


import com.teamE.commonAddsEvents.AddressRepo;
import com.teamE.events.data.EventsRepo;
import com.teamE.commonAddsEvents.Address;
import com.teamE.events.data.entity.Event;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventManager {

    private EventsRepo eventsRepo;
    private AddressRepo addressRepo;

    @Autowired
    public EventManager(EventsRepo eventsRepo, AddressRepo addressRepo) {
        this.eventsRepo = eventsRepo;
        this.addressRepo = addressRepo;
    }

    public Iterable<Event> findAll() {
        return eventsRepo.findAll();
    }

    public Optional<Event> findById(Long id) {
        return eventsRepo.findById(id);
    }

    public Event save(Event event) {
 /*       this.save(event.getAddress());*/
        return eventsRepo.save(event);
    }

    public Address save(Address address) {
        //addressRepo.save(event.getAddress());
        return addressRepo.save(address);
    }

    public Iterable<Event> findByScopeAndStudentHouse(Scope scope, StudentHouse studentHouse){
        return eventsRepo.findByScopeAndStudentHouse(scope, studentHouse);
    }
    public Iterable<Event> findByScope(Scope scope){
        return eventsRepo.findByScope(scope);
    }

    public Iterable<Event> findByScopeAndStudentHouseOrderByDateDesc(Scope scope, StudentHouse studentHouse) {
        return eventsRepo.findByScopeAndStudentHouseOrderByDateDesc(scope, studentHouse);
    }

    public Iterable<Event> findByScopeOrderByDateDesc(Scope scope){
        return  eventsRepo.findByScopeOrderByDateDesc(scope);
    }

    public void deleteById(Long id) {
        eventsRepo.deleteById(id);
    }

/*
    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        eventsRepo.save(new Event(new Address()));
    }*/

}
