package com.teamE.events.data.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String street;
    private int houseNumber;
    private int apartmentNumber;
    private String city;
    private String zip;

}



