package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Primary
public class DbUserService implements UserService {

    private final UserStorage storage;

    @Autowired
    public DbUserService(@Qualifier("dbUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public void addFriend(User user, User otherUser) {
        storage.addFriend(user, otherUser, true);
        if (storage.checkFriendship(otherUser, user)) {
            storage.addFriend(otherUser, user, true);
        }
    }

    @Override
    public void removeFriend(User user, User otherUser) {
        storage.removeFriend(user, otherUser);
        if (storage.checkFriendship(otherUser, user)) {
            storage.addFriend(otherUser, user, false); // возможно не сработает
        }
    }

    @Override
    public List<User> getMutualFriends(User user, User otherUser) {
        List<User> a = getFriendsList(user);
        List<User> b = getFriendsList(otherUser);
        a.retainAll(b);
        return a;
    }

    @Override
    public List<User> getFriendsList(User user) {
        return storage.getFriendsList(user);
    }

    @Override
    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public User getUserById(Integer id) {
        return storage.getUserById(id).orElseThrow(()
                -> new EntityNotFoundException("Фильм/Пользователь не найден.", getClass().toString()));
    }

    @Override
    public User addUser(User user) {
        return storage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return storage.updateUser(user);
    }
}
