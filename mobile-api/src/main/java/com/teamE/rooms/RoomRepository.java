package com.teamE.rooms;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(excerptProjection = RoomWithCategoryProjection.class)
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @RestResource(path = "byDsNumber")
    Page<RoomWithCategoryProjection> getAllByDsNumber(@Param("number") int dsNumber, Pageable p);

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
