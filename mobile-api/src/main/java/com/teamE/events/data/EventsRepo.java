package com.teamE.events.data;

import com.teamE.commonAddsEvents.Scope;
import com.teamE.events.data.entity.Event;
import com.teamE.users.StudentHouse;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventsRepo extends JpaRepository<Event, Long> {
    Iterable<Event> findByScopeAndStudentHouse(Scope scope, StudentHouse studentHouse);

    default Page<Event> findAllByScopeAndStudentHouseAndQuery(Scope scope, StudentHouse studentHouse, String query, Pageable pageable) {
        return this.findAllByScopeAndStudentHouseAndNameContainingIgnoreCaseOrStreetContainingIgnoreCaseOrCityContainingIgnoreCase(scope, studentHouse, query, query, query, pageable);
    }


    Page<Event> findAllByScopeAndStudentHouseAndNameContainingIgnoreCaseOrStreetContainingIgnoreCaseOrCityContainingIgnoreCase(Scope scope, StudentHouse studentHouse, String name, String street, String city, Pageable pageable);
  //  @Query("select e from Event e where (e.scope = ?1 or e.scope = ?4 or e.scope = ?5) and e.studentHouse in (?2,null) and (LOWER(e.name) LIKE LOWER(concat(?3, '%')) or LOWER(e.description) LIKE LOWER(concat(?3, '%')) or LOWER(e.street) LIKE LOWER(concat(?3, '%')) or LOWER(e.city) LIKE LOWER(concat(?3, '%')))")

    @Query("select e from Event e where e.scope >= ?1 and (e.studentHouse = ?2 or (e.studentHouse is null)) and (LOWER(e.name) LIKE LOWER(concat(?3, '%')) or LOWER(e.description) LIKE LOWER(concat(?3, '%')) or LOWER(e.street) LIKE LOWER(concat(?3, '%')) or LOWER(e.city) LIKE LOWER(concat(?3, '%')))")
    Page<Event> search(Scope scope, StudentHouse studentHouse, String query, Scope scope2, Scope scope3,Pageable pageable);


    Iterable<Event> findByScope(Scope scope);

    Iterable<Event> findByScopeAndStudentHouseOrderByDateDesc(Scope scope, StudentHouse studentHouse);

    Iterable<Event> findByScopeOrderByDateDesc(Scope scope);

    Iterable<Event> findAllById(Long id);
}
