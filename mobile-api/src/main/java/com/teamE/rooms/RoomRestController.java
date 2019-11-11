package com.teamE.rooms;


import com.teamE.security.UserService;
import com.teamE.security.jwt.JwtUtil;
import com.teamE.users.StudentHouse;
import com.teamE.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RequestMapping("rooms")
@RestController
public class RoomRestController {


    private RoomRepository roomRepository;
    private UserRepository userRepository;

    @Autowired
    public RoomRestController(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("room/{id}")
    public Room getRoomById(@PathVariable("id") int id) {
        return roomRepository.getRoomById(id);
    }
    @GetMapping("getAllFrom/{dsNumber}")
    public List<Room> getAllByDsNumber(@PathVariable("dsNumber") int dsNumber){

        return roomRepository.getAllByDsNumber(dsNumber);
    }
    @GetMapping("getAllForUser")
    public List<Room> getAllForUser(){

        return roomRepository.getAllByDsNumber(userRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getStudentHouse().getId());
    }

    @GetMapping("pies")
    public String pies() {
        return "pies";
    }

    @PostMapping("postRoom")
    public <S extends Room> S save(@RequestBody S s) {
        int h = roomRepository.countAllByDsNumber(s.getDsNumber());

        s.setId((s.getDsNumber() * 100 )+ h + 1);

        return roomRepository.save(s);
    }
}
