package com.teamE.rooms;

import com.teamE.common.UsersDemandingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("rooms")
public class RoomController extends UsersDemandingController {
    private RoomRepository roomRepository;
    private RoomProcessor roomProcessor;
    private RoomPOJOToRoomTransformer roomPOJOToRoomTransformer;

    @Autowired
    public RoomController(RoomRepository roomRepository, RoomProcessor roomProcessor, RoomPOJOToRoomTransformer roomPOJOToRoomTransformer) {
        super();
        this.roomRepository = roomRepository;
        this.roomProcessor = roomProcessor;
        this.roomPOJOToRoomTransformer = roomPOJOToRoomTransformer;
    }

    @GetMapping("available")
    public Page<RoomWithConfigurationProjection> getAllForUser(final Pageable pageable) {
        return roomRepository.getAllByDsNumber(getUserStudentHouseId(), pageable);
    }

    @PostMapping()
    public Room save(@RequestBody RoomPOJO pojo) {
        Room room = roomPOJOToRoomTransformer.transform(pojo);
        return roomRepository.save(room);
    }

    public Page<RoomWithConfigurationProjection> findForUser(final Pageable pageable, final String query) {
        return roomRepository.getAllByDsNumberAndQuery(getUserStudentHouseId(), query, pageable);
    }
}
