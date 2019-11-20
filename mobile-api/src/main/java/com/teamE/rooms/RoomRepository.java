package com.teamE.rooms;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {


        Room getRoomById(int id);
        List<Room> getAllByDsNumber(int dsNumber);
        <S extends Room> S save(S s);
        int countAllByDsNumber(int dsNumber);

}
