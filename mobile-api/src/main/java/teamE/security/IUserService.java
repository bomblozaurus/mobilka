package teamE.security;

import teamE.users.User;
import teamE.users.UserSignUpPOJO;

public interface IUserService{
    User registerNewUserAccount(UserSignUpPOJO account);
}
