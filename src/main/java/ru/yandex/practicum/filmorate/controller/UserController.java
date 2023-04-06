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

    /*PUT /users/{id}/friends/{friendId}  — добавление в друзья.
    DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.*/

    private final UserService userService;

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping(value = "/users")
    User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping(value = "/users")
    User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.addFriend(userService.getUserById(id), userService.getUserById(friendId));
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeFriend(userService.getUserById(id), userService.getUserById(friendId));
    }

    @GetMapping("/users/{id}/friends")
    List<User> getUserFriends(@PathVariable Integer id) {
        return userService.getFriendsList(userService.getUserById(id));
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    List<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getMutualFriends(userService.getUserById(id), userService.getUserById(otherId));
    }
}
