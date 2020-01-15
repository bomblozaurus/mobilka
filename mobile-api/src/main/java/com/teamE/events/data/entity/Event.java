package com.teamE.events.data.entity;


import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@Indexed
public class Event {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @Field(termVector = TermVector.YES)
    private String name;
    private Date date;
    @Field(termVector = TermVector.YES)
    private String street;
    private int houseNumber;
    private int apartmentNumber;
    @Field(termVector = TermVector.YES)
    private String city;
    private String zip;
    @Field(termVector = TermVector.YES)
    private String description;
    private Long mainImage;
    private Scope scope;
    @Enumerated(EnumType.STRING)
    private StudentHouse studentHouse;
}
