package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private List<User> users = new ArrayList<>();

    @GetMapping("/users")
    List<User> getAllUsers() {
        return users;
    }

    @PutMapping(value = "/user")
    User addUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

}
