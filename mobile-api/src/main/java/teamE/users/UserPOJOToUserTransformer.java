package teamE.users;

import org.springframework.stereotype.Service;
import teamE.common.Transformer;

@Service
public class UserPOJOToUserTransformer implements Transformer<UserPOJO, User> {
    @Override
    public User transform(UserPOJO pojo) {
        User user = User.builder()
                .email(pojo.getEmail())
                .firstName(pojo.getFirstName())
                .lastName(pojo.getLastName())
                .userRole(new UserRole(UserRoleType.USER))
                .password(pojo.getPassword())
                .indexNumber(pojo.getIndexNumber())
                .studentHouse(pojo.getStudentHouse())
                .build();

        return user;
    }
}
