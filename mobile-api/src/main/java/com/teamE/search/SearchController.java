package com.teamE.search;

import com.teamE.ads.controllers.AdsController;
import com.teamE.ads.data.entity.Ad;
import com.teamE.events.controllers.EventsController;
import com.teamE.events.data.entity.Event;
import com.teamE.rooms.Room;
import com.teamE.rooms.RoomController;
import com.teamE.rooms.RoomWithConfigurationDto;
import com.teamE.rooms.RoomWithConfigurationProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchController {

    private RoomController roomController;
    private EventsController eventsController;
    private AdsController adsController;

    @Autowired
    public SearchController(RoomController roomController, EventsController eventsController, AdsController adsController) {
        this.roomController = roomController;
        this.eventsController = eventsController;
        this.adsController = adsController;
    }

    @GetMapping("rooms")
    public Page<EntityModel<RoomWithConfigurationDto>> findAvailableRooms(final Pageable pageable, @RequestParam("query") final String query){
        return roomController.findForUser(pageable, query);
    }

    @GetMapping("events")
    public Page<EntityModel<Event>> findAvailableEvents(final Pageable pageable, @RequestParam("query") final String query){
        return eventsController.findForUser(pageable, query);
    }

    @GetMapping("ads")
    public Page<EntityModel<Ad>> findAvailableAds(final Pageable pageable, @RequestParam("query") final String query){
        return adsController.findForUser(pageable, query);
    }
}
