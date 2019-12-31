package com.teamE.reservations;

import com.teamE.rooms.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;

@RestResource(exported = false)
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    Page<Reservation> getAllByUserId(long userId, Pageable pageable);

    Page<Reservation> getAllByRoomIn(Collection<Room> rooms, Pageable pageable);
}
