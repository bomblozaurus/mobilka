package com.teamE.events.data.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.awt.*;
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
    @OneToOne(targetEntity=Address.class)
    private Address address;
    private String description;
    @Lob
    private byte[] image;
    private Scope scope;

//   public Event(String name, Date date, Address address, String description, byte[] image, Scope scope) {
//        this.name = name;
//        this.date = date;
//        this.address = address;
//        this.description = description;
//        this.image = image;
//        this.scope = scope;
//    }

   /* public Event(Address address) {
        this.address = address;
    }*/
}
