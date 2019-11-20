package com.teamE.ads.controllers;

import com.teamE.ads.data.entity.Ad;
import com.teamE.ads.managers.AdManager;
import com.teamE.commonAddsEvents.Address;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.commonAddsEvents.converters.ScopeConverter;
import com.teamE.commonAddsEvents.converters.StudentHouseConverter;
import com.teamE.users.StudentHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/ads")
public class AdsController {
    private AdManager adManager;

    @Autowired
    public AdsController(AdManager adManager) {
        this.adManager = adManager;
    }

    @GetMapping("/all")
    public Iterable<Ad> getAll() {
        return adManager.findAll();
    }

    @GetMapping
    public Optional<Ad> getById(@RequestParam Long index) {
        return adManager.findById(index);
    }

    @GetMapping("/scope")
    public Iterable<Ad> getByScope(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {
        if (scope.equals(Scope.DORMITORY)) {
            return adManager.findByScopeAndStudentHouse(scope, studentHouse);
        } else {
            return adManager.findByScope(scope);
        }
    }
    @GetMapping("/scopeOrderPriceDesc")
    public Iterable<Ad> getByScopeOrderByDateDesc(@RequestParam Scope scope, @RequestParam StudentHouse studentHouse) {
        if (scope.equals(Scope.DORMITORY)) {
            return adManager.findByScopeAndStudentHouseOrderByPriceDesc(scope, studentHouse);
        } else {
            return adManager.findByScopeOrderByPriceDesc(scope);
        }
    }

    @GetMapping("/address")
    public Address getAddress() {
        return new Address();
    }

    @PostMapping("/address")
    public Address addAddress(@RequestBody Address address) {
        return adManager.save(address);
    }

    @PostMapping
    public Ad addAd(@RequestBody Ad ad) {
        return adManager.save(ad);
    }

    @DeleteMapping
    public void deleteAd(@RequestParam Long index) {
        adManager.deleteById(index);
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(Scope.class, new ScopeConverter());
        webdataBinder.registerCustomEditor(StudentHouse.class, new StudentHouseConverter());
    }
}
