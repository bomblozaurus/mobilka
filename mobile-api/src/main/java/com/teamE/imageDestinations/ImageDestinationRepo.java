package com.teamE.imageDestinations;

import org.springframework.data.repository.CrudRepository;

public interface ImageDestinationRepo extends CrudRepository<ImageDestination, Long> {
    Iterable<ImageDestination> findAllByIdDestinationAndDestination(Long idDestination, Destination destination);

    boolean existsByIdAndIdDestination(Long id, Long idDestination);
}
