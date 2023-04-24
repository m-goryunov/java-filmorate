package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserServiceImpl(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    @Override
    public void addFriend(User user, User otherUser) {
        friendStorage.addFriend(user, otherUser);
    }

    @Override
    public void removeFriend(User user, User otherUser) {
        friendStorage.removeFriend(user, otherUser);
    }

    @Override
    public List<User> getMutualFriends(User user, User otherUser) {
        return friendStorage.getMutualFriends(user, otherUser);
    }

    @Override
    public List<User> getFriendsList(User user) {
        return friendStorage.getFriendsList(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    @Override
    public User addUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        userStorage.updateUser(user);
        return user;
    }
}
