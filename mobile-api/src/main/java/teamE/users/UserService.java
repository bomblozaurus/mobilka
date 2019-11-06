package teamE.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teamE.users.exceptions.EmailAlreadyUsedException;
import teamE.users.exceptions.UserException;

@Service("userService")
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPOJOToUserTransformer transformer;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User registerNewUserAccount(UserPOJO account) throws UserException {
        if (emailAlreadyExists(account)) {
            throw new EmailAlreadyUsedException("There is an account with that email address: " + account.getEmail());
        }

        account.setPassword(encoder.encode(account.getPassword()));
        User user = transformer.transform(account);

        return userRepository.save(user);
    }


    private boolean emailAlreadyExists(UserPOJO account) {
        User alreadyRegistered = userRepository.getByEmail(account.getEmail());

        return alreadyRegistered != null;
    }
}
