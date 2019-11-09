package com.teamE.security;

import com.teamE.users.User;
import com.teamE.users.UserSignUpPOJO;

public interface IUserService{
    User registerNewUserAccount(UserSignUpPOJO account);
}
