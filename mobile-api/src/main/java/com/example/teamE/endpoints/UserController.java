package com.example.teamE.endpoints;

import com.example.teamE.users.User;
import com.example.teamE.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "user/{id}")
    public User getUser(@PathVariable("id") long id) {
        return userRepository.getById(id);
    }

    @GetMapping(path = "/users")
    public Iterable<User> getUsers(){
        return userRepository.findAll();
    }
}
