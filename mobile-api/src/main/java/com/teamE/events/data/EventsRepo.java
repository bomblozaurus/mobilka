package com.teamE.events.data;

import com.teamE.commonAddsEvents.Scope;
import com.teamE.events.data.entity.Event;
import com.teamE.users.StudentHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepo extends JpaRepository<Event, Long> {
    Iterable<Event> findByScopeAndStudentHouse(Scope scope, StudentHouse studentHouse);

    Iterable<Event> findByScope(Scope scope);

    Iterable<Event> findByScopeAndStudentHouseOrderByDateDesc(Scope scope, StudentHouse studentHouse);

    Iterable<Event> findByScopeOrderByDateDesc(Scope scope);
}
