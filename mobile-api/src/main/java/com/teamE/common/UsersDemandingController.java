package com.teamE.common;

import com.teamE.users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class UsersDemandingController {

    @Autowired
    private UserRepository userRepository;

    public StudentHouse getUserStudentHouse() {
        return this.getUser().getStudentHouse();
    }

    public User getUser() {
        return userRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public int getUserStudentHouseId() {
        return this.getUserStudentHouse().getId();
    }

    public long getUserId() {
        return this.getUser().getId();
    }

    public boolean isUserKeyholder() {
        return this.getUser().getUserRoles().stream().map(UserRole::getType).anyMatch(role -> Objects.equals(role, UserRoleType.KEYHOLDER));
    }
}
