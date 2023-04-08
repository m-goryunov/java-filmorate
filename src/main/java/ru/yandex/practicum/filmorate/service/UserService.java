package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void addFriend(User user, User otherUser);

    void removeFriend(User user, User otherUser);

    List<User> getMutualFriends(User user, User otherUser);

    List<User> getAllUsers();

    User getUserById(Integer id);

    User addUser(User user);

    User updateUser(User user);

    List<User> getFriendsList(User user);
}
