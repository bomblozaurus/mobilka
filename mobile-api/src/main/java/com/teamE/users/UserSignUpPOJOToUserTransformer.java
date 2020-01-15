package com.teamE.users;

import com.teamE.common.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class UserSignUpPOJOToUserTransformer implements Transformer<UserSignUpPOJO, User> {
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Override
    public User transform(UserSignUpPOJO pojo) {

        return User.builder()
                .email(pojo.getEmail())
                .firstName(pojo.getFirstName())
                .lastName(pojo.getLastName())
                .userRole(createRoleIfNotFound("KEYHOLDER"))
                .password(pojo.getPassword())
                .indexNumber(pojo.getIndexNumber())
                .studentHouse(pojo.getStudentHouse())
                .build();
    }

    @Transactional
    UserRole createRoleIfNotFound(String name) {
        UserRole role = userRoleRepository.getByType(UserRoleType.valueOf(name));
        if (role == null) {
            role = new UserRole(UserRoleType.valueOf(name));
            userRoleRepository.save(role);
        }
        return role;
    }
}
