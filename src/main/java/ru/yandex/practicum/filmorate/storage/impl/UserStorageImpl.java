package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.util.SqlMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Component
@Slf4j
public class UserStorageImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlMapper mapper;

    @Autowired
    public UserStorageImpl(JdbcTemplate jdbcTemplate, SqlMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM USER_FILMORATE";
        return jdbcTemplate.query(sqlQuery, mapper::mapRowToUser);
    }

    @Override
    public User getUserById(Integer id) {
        try {
            String sqlQuery = "SELECT ID, EMAIL, LOGIN, BIRTHDAY,NAME " +
                    "FROM USER_FILMORATE " +
                    "WHERE ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, mapper::mapRowToUser, id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Ошибка при получении пользователя", e.getMessage());
        }
    }

    @Override
    public User createUser(User user) {
        log.info("Попытка добавить пользователя");
        String sqlQuery = "INSERT INTO USER_FILMORATE(EMAIL, LOGIN, BIRTHDAY, NAME) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setDate(3, Date.valueOf(user.getBirthday()));
            stmt.setString(4, user.getName());
            return stmt;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Пользователь добавлен. id: {}", user.getId());
        return user;
    }

    @Override
    public void updateUser(User user) {
        log.info("Попытка обновить пользователя id: {}", user.getId());
        String sqlQuery = "UPDATE USER_FILMORATE SET " +
                "EMAIL = ?, LOGIN = ?, BIRTHDAY = ?, NAME = ? " +
                "WHERE ID = ?";
        int rows = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getName(),
                user.getId());
        if (rows == 0) {
            throw new NoSuchElementException("Пользователь не существует.");
        }
        log.info("Пользователь обновлён. id: {}", user.getId());
    }
}
