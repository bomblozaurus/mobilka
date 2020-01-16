package com.teamE.users;

import com.teamE.commonAddsEvents.Scope;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.security.sasl.SaslClient;

@Data
@NoArgsConstructor
public class UserSignUpPOJO {
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Integer indexNumber;
    private StudentHouse studentHouse;
    private Scope scope;
}
