package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidateExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final ValidateService validateUser;
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 0;


    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    public User addUser(User user) {
        validateUser.validateUser(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен.{}", user.getId());
        return user;
    }

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
