package com.teamE.ads.data.entity;

import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AdPOJO {
    private Long id;
    private String name;
    BigDecimal price;
    private String street;
    private int houseNumber;
    private int apartmentNumber;
    private String city;
    private String zip;
    private String description;
    private Long mainImage;
    private Scope scope;
    private StudentHouse studentHouse;
    private List<Long> additionalImages;
}
