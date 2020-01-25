package com.teamE.ads.data.entity;

import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ad {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    BigDecimal price;
    private String street;
    private int houseNumber;
    private int apartmentNumber;
    private LocalDateTime date;
    private Long userID;
    private String city;
    private String zip;
    @Lob
    @Column
    private String description;
    private Long mainImage;
    private Scope scope;
    @Enumerated(EnumType.STRING)
    private StudentHouse studentHouse;
}
