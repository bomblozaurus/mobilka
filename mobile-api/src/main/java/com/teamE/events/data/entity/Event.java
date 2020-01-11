package com.teamE.events.data.entity;


import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
public class Event {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String name;
    private Date date;
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
