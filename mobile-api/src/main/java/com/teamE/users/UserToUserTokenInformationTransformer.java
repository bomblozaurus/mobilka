package com.teamE.users;

import com.teamE.common.Transformer;

public class UserToUserTokenInformationTransformer implements Transformer<User, UserTokenInformation> {
    @Override
    public UserTokenInformation transform(User user) {
        UserRole role = user.getUserRole();
        return UserTokenInformation.builder()
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .role(role.toString())
                .build();
    }
}
