package com.teamE.rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RoomManager {
    private RoomRepository roomRepo;

    @Autowired
    public RoomManager(RoomRepository roomRepo)
    {
        this.roomRepo = roomRepo;
    }

    public Optional<Room> findById(int id) {
        return roomRepo.findById(id);
    }
    public Iterable<Room> findAll(int id) {
        return roomRepo.findAll();

    }
    public Room save(Room room) {
        return roomRepo.save(room);

    }

    public void deleteById(int id) {
        roomRepo.deleteById(id);
    }


}
