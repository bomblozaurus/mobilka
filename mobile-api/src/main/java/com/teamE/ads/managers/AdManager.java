package com.teamE.ads.managers;

import com.teamE.ads.data.AdsRepo;
import com.teamE.ads.data.entity.Ad;
import com.teamE.commonAddsEvents.Address;
import com.teamE.commonAddsEvents.AddressRepo;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdManager {
    private AdsRepo adsRepo;
    private AddressRepo addressRepo;

    @Autowired
    public AdManager(AdsRepo adsRepo, AddressRepo addressRepo) {
        this.adsRepo = adsRepo;
        this.addressRepo = addressRepo;
    }

    public Iterable<Ad> findAll() {
        return adsRepo.findAll();
    }

    public Optional<Ad> findById(Long id) {
        return adsRepo.findById(id);
    }

    public Ad save(Ad ad) {
        this.save(ad.getAddress());
        return adsRepo.save(ad);
    }

    public Address save(Address address) {
        //addressRepo.save(ad.getAddress());
        return addressRepo.save(address);
    }

    public Iterable<Ad> findByScopeAndStudentHouse(Scope scope, StudentHouse studentHouse){
        return adsRepo.findByScopeAndStudentHouse(scope, studentHouse);
    }
    public Iterable<Ad> findByScope(Scope scope){
        return adsRepo.findByScope(scope);
    }

    public Iterable<Ad> findByScopeAndStudentHouseOrderByPriceDesc(Scope scope, StudentHouse studentHouse) {
        return adsRepo.findByScopeAndStudentHouseOrderByPriceDesc(scope, studentHouse);
    }

    public Iterable<Ad> findByScopeOrderByPriceDesc(Scope scope){
        return  adsRepo.findByScopeOrderByPriceDesc(scope);
    }
    public void deleteById(Long id) {
        adsRepo.deleteById(id);
    }

}
