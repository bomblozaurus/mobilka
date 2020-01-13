package com.teamE.reservations;

import com.teamE.rooms.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RepositoryRestResource(exported = false, excerptProjection = SimpleReservationProjection.class)
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    Page<SimpleReservationProjection> getAllByUserId(long userId, Pageable pageable);

    Page<SimpleReservationProjection> getAllByRoomIn(Collection<Room> rooms, Pageable pageable);

    List<Reservation> getAllByRoomIdAndDateTimeBetween(long room_id, LocalDateTime start, LocalDateTime end);

    List<Reservation> getAllByRoomIdAndDateTime(long room_id, LocalDateTime start);



    @Query(value = "SELECT * FROM reservations  WHERE room_id = ?1 AND YEAR(date_time)  = ?2 and MONTH(date_time)= ?3 and EXTRACT(DAY FROM date_time) = ?4 ", nativeQuery =  true)
    List<Reservation> getAllByRoomIdAndCalendarDate(long room_id, int year, int month, int day);

    default Page<SimpleReservationProjection> getAllByUserIdAndRoomInAndQuery(long userId, Collection<Room> room, Pageable pageable) {
        return getAllByUserIdAndRoomIn(userId, room, pageable);
    }

    Page<SimpleReservationProjection> getAllByUserIdAndRoomIn(long user_id, Collection<Room> room, Pageable pageable);

}
