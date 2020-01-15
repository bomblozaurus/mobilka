package com.teamE.users;

import com.teamE.common.Transformer;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserToUserTokenInformationTransformer implements Transformer<User, UserTokenInformation> {
    @Override
    public UserTokenInformation transform(User user) {
        List<String> roles = user.getUserRoles().stream().map(Object::toString).collect(Collectors.toList());
        return UserTokenInformation.builder()
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .role(StringUtils.join(roles, ','))
                .build();
    }
}
