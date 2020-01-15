package com.teamE.users;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTokenInformation {
    private String email;
    private String name;
    private String role;

}




