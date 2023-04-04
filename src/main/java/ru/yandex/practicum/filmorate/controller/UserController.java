package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    /*PUT /users/{id}/friends/{friendId}  — добавление в друзья.
    DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.*/

    private final UserService userService;

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(value = "/users")
    User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping(value = "/users")
    User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/user/{userId}")
    public User addFriend(@PathVariable("userId") Integer userId) {
        return postService.findPostById(userId);
    }

}
