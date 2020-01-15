package com.teamE.common;

import com.teamE.users.StudentHouse;
import com.teamE.users.User;
import com.teamE.users.UserRepository;
import com.teamE.users.UserRoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

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
        return this.getUser().getUserRole().getType().name().equals(UserRoleType.KEYHOLDER);
    }
}
