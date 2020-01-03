package com.teamE.reservations;

import com.teamE.rooms.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource(exported = false, excerptProjection = SimpleReservationProjection.class)
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    Page<SimpleReservationProjection> getAllByUserId(long userId, Pageable pageable);

    Page<SimpleReservationProjection> getAllByRoomIn(Collection<Room> rooms, Pageable pageable);

    default Page<SimpleReservationProjection> getAllByUserIdAndRoomInAndQuery(long userId, Collection<Room> room, Pageable pageable) {
        return getAllByUserIdAndRoomIn(userId, room, pageable);
    }

    Page<SimpleReservationProjection> getAllByUserIdAndRoomIn(long user_id, Collection<Room> room, Pageable pageable);

}
