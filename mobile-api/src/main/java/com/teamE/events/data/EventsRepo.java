package com.teamE.events.data;

import com.teamE.events.data.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepo extends CrudRepository<Event,Long> {
}
