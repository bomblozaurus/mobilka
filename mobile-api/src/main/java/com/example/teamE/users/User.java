package com.example.teamE.users;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Data
//@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Table(name = "USERS_T")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    int indexNumber;
    StudentHouse studentHouse;
}



