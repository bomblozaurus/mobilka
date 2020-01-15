package com.teamE.ads.data.entity;

import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ad {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
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
    @Enumerated(EnumType.STRING)
    private StudentHouse studentHouse;
}
