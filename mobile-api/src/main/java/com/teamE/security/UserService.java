package com.teamE.security;

import com.teamE.users.*;
import com.teamE.users.exceptions.EmailAlreadyUsedException;
import com.teamE.users.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserSignUpPOJOToUserTransformer transformer;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(PasswordEncoder encoder, UserRepository userRepository, UserSignUpPOJOToUserTransformer transformer) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.transformer = transformer;
    }

    @Override
    public User registerNewUserAccount(UserSignUpPOJO account) throws UserException {
        if (emailAlreadyExists(account)) {
            throw new EmailAlreadyUsedException("There is an account with that email address: " + account.getEmail());
        }

        account.setPassword(encoder.encode(account.getPassword()));
        User user = transformer.transform(account);

        return userRepository.save(user);
    }

    public UserTokenInformation getUserDetailsForToken(String email) {
        User user = userRepository.getByEmail(email);
        UserToUserTokenInformationTransformer transformer = new UserToUserTokenInformationTransformer();
        return transformer.transform(user);
    }


    private boolean emailAlreadyExists(UserSignUpPOJO account) {
        User alreadyRegistered = userRepository.getByEmail(account.getEmail());

        return alreadyRegistered != null;
    }
}
