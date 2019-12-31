package com.teamE.rooms;

import com.teamE.users.User;
import com.teamE.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("rooms")
@RestController
public class RoomController {
    private RoomRepository roomRepository;
    private RoomProcessor roomProcessor;
    private RoomPOJOToRoomTransformer roomPOJOToRoomTransformer;

    private UserRepository userRepository;

    @Autowired
    public RoomController(RoomRepository roomRepository, RoomProcessor roomProcessor, RoomPOJOToRoomTransformer roomPOJOToRoomTransformer, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.roomProcessor = roomProcessor;
        this.roomPOJOToRoomTransformer = roomPOJOToRoomTransformer;
        this.userRepository = userRepository;
    }

    @GetMapping("available")
    public Page<RoomWithCategoryProjection> getAllForUser(final Pageable pageable) {
        User user = userRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return roomRepository.getAllByDsNumber(user.getStudentHouse().getId(), pageable);
    }

    @PostMapping()
    public Room save(@RequestBody RoomPOJO pojo) {
        Room room = roomPOJOToRoomTransformer.transform(pojo);
        return roomRepository.save(room);
    }
}
