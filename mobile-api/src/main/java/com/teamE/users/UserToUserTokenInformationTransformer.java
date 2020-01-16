package com.teamE.users;

import com.teamE.common.Transformer;
import org.springframework.stereotype.Service;

@Service
public class UserToUserTokenInformationTransformer implements Transformer<User, UserTokenInformation> {
    @Override
    public UserTokenInformation transform(User user) {
        UserRole role = user.getUserRole();
        return UserTokenInformation.builder()
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .role(role.getType().name())
                .scope(user.getScope())
                .build();
    }
}
