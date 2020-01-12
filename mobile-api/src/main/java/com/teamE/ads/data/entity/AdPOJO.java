package com.teamE.ads.data.entity;

import com.teamE.commonAddsEvents.Address;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AdPOJO {
    private Long id;
    private String name;
    BigDecimal price;
    private Address address;
    private String description;
    private Long mainImage;
    private Scope scope;
    private StudentHouse studentHouse;
    private List<Long> additionalImages;
}
