package com.teamE.users;

import com.teamE.common.Transformer;
import org.springframework.stereotype.Component;

@Component
public class UserSignUpPOJOToUserTransformer implements Transformer<UserSignUpPOJO, User> {
    @Override
    public User transform(UserSignUpPOJO pojo) {

        return User.builder()
                .email(pojo.getEmail())
                .firstName(pojo.getFirstName())
                .lastName(pojo.getLastName())
                .userRole(new UserRole(UserRoleType.USER))
                .password(pojo.getPassword())
                .indexNumber(pojo.getIndexNumber())
                .studentHouse(pojo.getStudentHouse())
                .build();
    }
}
