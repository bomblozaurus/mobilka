package com.teamE.search;

import com.teamE.events.controllers.EventsController;
import com.teamE.events.data.entity.Event;
import com.teamE.rooms.RoomController;
import com.teamE.rooms.RoomWithConfigurationProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchController {

    private RoomController roomController;
    private EventsController eventsController;

    @Autowired
    public SearchController(RoomController roomController, EventsController eventsController) {
        this.roomController = roomController;
        this.eventsController = eventsController;
    }

    @GetMapping("rooms")
    public Page<RoomWithConfigurationProjection> findAvailableRooms(final Pageable pageable, @RequestParam("query") final String query){
        return roomController.findForUser(pageable, query);
    }

    @GetMapping("events")
    public Page<Event> findAvailableEvents(final Pageable pageable, @RequestParam("query") final String query){
        return eventsController.findForUser(pageable, query);
    }
}
