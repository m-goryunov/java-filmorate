package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class InMemoryUserService implements UserService {

    private final UserStorage storage;

    @Autowired
    public InMemoryUserService(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public User getUserById(Integer id) {
        return storage.getUserById(id);
    }

    @Override
    public User addUser(User user) {
        return storage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    @Override
    public void addFriend(User user, User otherUser) {
        if (storage.getUserById(user.getId()) == null || storage.getUserById(otherUser.getId()) == null) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        user.addFriend(otherUser);
        otherUser.addFriend(user);
    }

    @Override
    public void removeFriend(User user, User otherUser) {
        user.removeFriend(otherUser);
        otherUser.removeFriend(user);
    }

    @Override
    public List<User> getMutualFriends(User user, User otherUser) {
        if (storage.getUserById(user.getId()) == null || storage.getUserById(otherUser.getId()) == null) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        List<User> a = getFriendsList(user);
        List<User> b = getFriendsList(otherUser);

        a.retainAll(b);

        return a;
    }

    @Override
    public List<User> getFriendsList(User user) {
        List<User> friends = new ArrayList<>();
        for (Integer id : user.getFriends()) {
            friends.add(storage.getUserById(id));
        }
        return friends;
    }

}
