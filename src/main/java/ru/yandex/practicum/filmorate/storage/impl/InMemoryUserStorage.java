package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {


    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    @Override
    public void addFriend(User user, User otherUser) {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public void removeFriend(User user, User otherUser) {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public List<User> getFriendsList(User user) {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен.{}", user.getId());
        return user;
    }

    @Override
    public void updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь обновлён.{}", user.getId());
        } else {
            throw new EntityNotFoundException("Пользователь не существует.", getClass().toString());
        }
    }
}
