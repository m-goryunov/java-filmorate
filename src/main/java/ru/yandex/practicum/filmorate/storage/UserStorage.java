package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    void addFriend(User user, User otherUser);

    void removeFriend(User user, User otherUser);

    List<User> getFriendsList(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Integer id);

    User createUser(User user);

    void updateUser(User user);
}
