package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private UserService service = new UserService();

    @GetMapping("/users")
    List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping(value = "/users")
    User addUser(@RequestBody User user) {
        return service.addUser(user);
    }

    @PutMapping(value = "/users")
    User updateUser(@RequestBody User user) {
        return service.updateUser(user);
    }

}
