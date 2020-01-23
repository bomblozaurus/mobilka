package com.teamE.ads.data;

import com.teamE.ads.data.entity.Ad;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepo extends CrudRepository<Ad,Long> {
    Iterable<Ad> findByScopeAndStudentHouse(Scope scope, StudentHouse studentHouse);
    Iterable<Ad> findByScope(Scope scope);

    Iterable<Ad> findAllById(Long id);

    default Page<Ad> findAllByScopeAndStudentHouseAndQuery(Scope scope, StudentHouse studentHouse, String query, Pageable pageable) {
        return this.findAllByScopeAndStudentHouseAndNameContainingIgnoreCaseOrStreetContainingIgnoreCaseOrCityContainingIgnoreCase(scope, studentHouse, query, query, query, pageable);
    }

    Page<Ad> findAllByScopeAndStudentHouseAndNameContainingIgnoreCaseOrStreetContainingIgnoreCaseOrCityContainingIgnoreCase(Scope scope, StudentHouse studentHouse, String name, String street, String city, Pageable pageable);

    @Query("select e from Ad e where e.scope >= ?1 and (e.studentHouse = ?2 or (e.studentHouse is null)) and (LOWER(e.name) LIKE LOWER(concat(?3, '%')) or LOWER(e.description) LIKE LOWER(concat(?3, '%')) or LOWER(e.street) LIKE LOWER(concat(?3, '%')) or LOWER(e.city) LIKE LOWER(concat(?3, '%')))")
    Page<Ad> search(Scope scope, StudentHouse studentHouse, String query, Pageable pageable);

    Iterable<Ad> findByScopeAndStudentHouseOrderByPriceDesc(Scope scope, StudentHouse studentHouse);
    Iterable<Ad> findByScopeOrderByPriceDesc(Scope scope);
}
