package com.teamE.events.data;

import com.teamE.events.data.entity.Event;
import com.teamE.events.data.entity.Scope;
import com.teamE.users.StudentHouse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepo extends CrudRepository<Event,Long> {
    Iterable<Event> findByScopeAndStudentHouse(Scope scope, StudentHouse studentHouse);
    Iterable<Event> findByScope(Scope scope);


    Iterable<Event> findByScopeAndStudentHouseOrderByDateDesc(Scope scope, StudentHouse studentHouse);
    Iterable<Event> findByScopeOrderByDateDesc(Scope scope);
}
