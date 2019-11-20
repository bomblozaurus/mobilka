package com.teamE.ads.data;

import com.teamE.ads.data.entity.Ad;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepo extends CrudRepository<Ad,Long> {
    Iterable<Ad> findByScopeAndStudentHouse(Scope scope, StudentHouse studentHouse);
    Iterable<Ad> findByScope(Scope scope);


    Iterable<Ad> findByScopeAndStudentHouseOrderByPriceDesc(Scope scope, StudentHouse studentHouse);
    Iterable<Ad> findByScopeOrderByPriceDesc(Scope scope);
}
