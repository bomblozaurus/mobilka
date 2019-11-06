package teamE.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import teamE.users.User;
import teamE.users.UserPOJO;
import teamE.users.UserService;

@RestController

public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "signUp", consumes = "application/json")
    @ResponseBody
    public ResponseEntity signUp(@RequestBody UserPOJO accountDetails) {
        createUserAccount(accountDetails);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    private void createUserAccount(UserPOJO accountDetails) {
        User user = userService.registerNewUserAccount(accountDetails);

        //TODO JWT z danymi z POJO i Usera
    }
}
