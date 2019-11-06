package com.example.teamE.endpoints;

import com.example.teamE.endpoints.exceptions.UserNotValidException;
import com.example.teamE.users.User;
import com.example.teamE.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController

public class UnsecuredController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "signUp", consumes = "application/json")
    @ResponseBody
    public ResponseEntity signUp(@RequestBody User user) {
        try{
            userRepository.save(user);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e){
            throw new UserNotValidException("User not valid!");
        }
    }
}
