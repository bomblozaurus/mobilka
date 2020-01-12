package com.teamE.ads.data.entity;

import com.teamE.commonAddsEvents.Address;
import com.teamE.commonAddsEvents.Scope;
import com.teamE.imageDestinations.ImageDestination;
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
    @OneToOne(targetEntity= Address.class)
    private Address address;
    private String description;
    private Long mainImage;
    private Scope scope;
    private StudentHouse studentHouse;
}
