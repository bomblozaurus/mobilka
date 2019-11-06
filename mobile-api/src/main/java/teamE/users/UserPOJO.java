package teamE.users;

import lombok.Data;
import lombok.NoArgsConstructor;
import teamE.users.StudentHouse;

@Data
@NoArgsConstructor
public class UserPOJO {
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private int indexNumber;
    private StudentHouse studentHouse;
}
