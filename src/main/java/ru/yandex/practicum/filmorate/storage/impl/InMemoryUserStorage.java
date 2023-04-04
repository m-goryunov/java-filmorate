package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateExeption;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.util.ValidateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final ValidateService validateUser;
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public User addUser(User user) {
        validateUser.validateUser(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен.{}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь обновлён.{}", user.getId());
        } else {
            throw new ValidateExeption("Обновление несуществующего пользователя.");
        }
        return user;
    }
}
