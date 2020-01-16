package com.teamE.users;

import com.teamE.commonAddsEvents.Scope;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTokenInformation {
    private String email;
    private String name;
    private String role;
    private Scope scope;
}




