package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/users")
    List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping(value = "/users")
    User addUser(@Valid @RequestBody User user) {
        return service.addUser(user);
    }

    @PutMapping(value = "/users")
    User updateUser(@Valid @RequestBody User user) {
        return service.updateUser(user);
    }

}
