package com.teamE.rooms;

import com.teamE.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(excerptProjection = RoomWithConfigurationProjection.class)
public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<RoomWithConfigurationProjection> getAllByDsNumber(int dsNumber, Pageable p);

    List<Room> getAllByKeyholder(User keyholder);

    List<Room> findAllById(Long id);

    default Page<RoomWithConfigurationProjection> getAllByDsNumberAndQuery(int dsNumber, String query, Pageable pageable) {
        return this.getAllByDsNumberAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(dsNumber, query, query, pageable);
    }

    @Query("select e from Room e where e.dsNumber = ?1 and (LOWER(e.name) LIKE LOWER(concat(?2, '%')) or LOWER(e.description) LIKE LOWER(concat(?2, '%')))")
    Page<RoomWithConfigurationProjection> search(int dsNumber, String query, Pageable pageable);

    Page<RoomWithConfigurationProjection> getAllByDsNumberAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(int dsNumber, String name, String description, Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends Room> List<S> saveAll(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    <S extends Room> S saveAndFlush(S s);

    @Override
    @RestResource(exported = false)
    <S extends Room> S save(S s);
}
