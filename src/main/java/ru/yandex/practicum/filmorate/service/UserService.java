package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidateExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserService {

    private ValidateService validateUser = new ValidateService();
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 0;


    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    public User addUser(User user) {
        validateUser.validateUser(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь " + user.getId() + " добавлен.");
        return user;
    }

    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getId() + " обновлён.");
        }
        else {
            throw new ValidateExeption("Обновление несуществующего пользователя.");
        }
        return user;
    }
}
