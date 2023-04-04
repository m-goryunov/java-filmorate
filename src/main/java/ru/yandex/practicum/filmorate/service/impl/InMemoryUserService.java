package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Service
public class InMemoryUserService implements UserService {

    private UserStorage storage;

    @Autowired
    public InMemoryUserService(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<User> getAllUsers() {
        return storage.getAllUsers();
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
        user.addFriend(otherUser);
        otherUser.addFriend(user);
    }

    @Override
    public void removeFriend(User user, User otherUser) {
        user.removeFriend(otherUser);
        otherUser.removeFriend(user);
    }

    @Override
    public Set<Integer> getMutualFriends(User user, User otherUser) {
        Set<Integer> a = user.getFriends();
        Set<Integer> b = otherUser.getFriends();

        a.retainAll(b);

        return a;
    }

}
