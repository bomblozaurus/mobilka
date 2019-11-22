package com.teamE.security.authentication;

import com.teamE.security.jwt.JwtUtil;
import com.teamE.users.UserSignInPOJO;
import com.teamE.users.UserTokenInformation;
import com.teamE.users.exceptions.UserDisabledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.teamE.security.UserService;
import com.teamE.security.jwt.JwtResponse;
import com.teamE.users.UserSignUpPOJO;
import com.teamE.users.exceptions.BadUserCredentialsException;
import com.teamE.users.exceptions.UserException;

@RestController

public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(path = "signUp", consumes = "application/json")
    @ResponseBody
    public ResponseEntity signUp(@RequestBody UserSignUpPOJO accountDetails) {
        return createUserAccount(accountDetails) ? new ResponseEntity(HttpStatus.CREATED) : new ResponseEntity(HttpStatus.CONFLICT);
    }

    @PostMapping(path = "logIn", consumes = "application/json")
    @ResponseBody
    public ResponseEntity logIn(@RequestBody UserSignInPOJO authenticationDetails, @RequestHeader("Device-info") String device) throws Exception {
        try {
            authenticate(authenticationDetails.getEmail(), authenticationDetails.getPassword());
        } catch (UserException e) {
            return new ResponseEntity(e, HttpStatus.UNAUTHORIZED);
        }

        UserTokenInformation userTokenInformation = userService.getUserDetailsForToken(authenticationDetails.getEmail());
        String token = jwtUtil.generateToken(userTokenInformation, device);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping(path = "verifyToken")
    public ResponseEntity verifyToken(@RequestHeader("Device-info") String device) throws Exception {
        return new ResponseEntity(HttpStatus.OK);
    }

    private void authenticate(String email, String password) throws Exception {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new Exception("Empty credentials");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException disabled) {
            throw new UserDisabledException("User disabled");
        } catch (BadCredentialsException badCredentials) {
            throw new BadUserCredentialsException("Invalid credentials");
        }
    }

    private boolean createUserAccount(UserSignUpPOJO accountDetails) {
        try {
            userService.registerNewUserAccount(accountDetails);
            return true;
        } catch (UserException ue) {
            return false;
        }
    }
}
