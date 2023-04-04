package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    void addFriend(User user, User otherUser);

    void removeFriend(User user, User otherUser);

    Set<Integer> getMutualFriends(User user, User otherUser);

    List<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);
}
