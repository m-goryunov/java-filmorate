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
import java.util.Optional;

@Component
@Slf4j
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlMapper map;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate, SqlMapper map) {
        this.jdbcTemplate = jdbcTemplate;
        this.map = map;
    }

    @Override
    public void addFriend(User user, User otherUser) {
        String sqlQuery = "INSERT INTO SCHEMA.USER_FRIENDS (USER_ID, FRIEND_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, user.getId(), otherUser.getId());
    }

    @Override
    public void removeFriend(User user, User otherUser) {
        String sqlQuery = "DELETE FROM SCHEMA.USER_FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        int rows = jdbcTemplate.update(sqlQuery, user.getId(), otherUser.getId());
        if (rows == 0) {
            throw new NoSuchElementException("Друг отсутствует.");
        }
    }

    @Override
    public List<User> getFriendsList(User user) {
        String sql = "SELECT ID, NAME, LOGIN, EMAIL, BIRTHDAY " +
                "FROM SCHEMA.USER_FILMORATE " +
                "WHERE ID IN" +
                "(SELECT FRIEND_ID FROM SCHEMA.USER_FRIENDS WHERE USER_ID = ?)";

        return jdbcTemplate.query(sql, map::mapRowToUser, user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM SCHEMA.USER_FILMORATE";
        return jdbcTemplate.query(sqlQuery, map::mapRowToUser);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        try {
            String sqlQuery = "SELECT ID, EMAIL, LOGIN, BIRTHDAY,NAME " +
                    "FROM SCHEMA.USER_FILMORATE " +
                    "WHERE ID = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, map::mapRowToUser, id));
        } catch (Exception e) {
            throw new EntityNotFoundException("Ошибка при получении пользователя", e.getMessage());
        }
    }

    @Override
    public User createUser(User user) {
        log.info("Попытка добавить пользователя");
        String sqlQuery = "INSERT INTO SCHEMA.USER_FILMORATE(EMAIL, LOGIN, BIRTHDAY, NAME) " +
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
        String sqlQuery = "UPDATE SCHEMA.USER_FILMORATE SET " +
                "EMAIL = ?, LOGIN = ?, BIRTHDAY = ?, NAME = ? " +
                "WHERE ID = ?";
        int rows = jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getName()
                , user.getId());
        if (rows == 0) {
            throw new NoSuchElementException("Пользователь не существует.");
        }
        log.info("Пользователь обновлён. id: {}", user.getId());
    }

}
