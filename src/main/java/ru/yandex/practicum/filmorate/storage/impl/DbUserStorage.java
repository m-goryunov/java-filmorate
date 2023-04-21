package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(User user, User otherUser, boolean friendshipStatus) {
        String sql = "insert into SCHEMA.USER_FRIENDS(USER_ID, FRIEND_ID, FRIENDSHIP_STATUS)" +
                "VALUES (?,?,?)";
        jdbcTemplate.update(sql, user.getId(), otherUser.getId(), friendshipStatus);
    }

    @Override
    public void removeFriend(User user, User otherUser) {
        String sql = "delete from SCHEMA.USER_FRIENDS " +
                "where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, user.getId(), otherUser.getId());
    }


    @Override
    public boolean checkFriendship(User user, User otherUser) {
        String sqlQuery = "select FRIENDSHIP_STATUS " +
                "from SCHEMA.USER_FRIENDS " +
                "where USER_ID = ? AND FRIEND_ID = ?";
        SqlRowSet response = jdbcTemplate.queryForRowSet(sqlQuery, user.getId(), otherUser.getId());

        return response.getBoolean("FRIENDSHIP_STATUS");
    }

    @Override
    public List<User> getFriendsList(User user) {
        String sql = "SELECT * FROM SCHEMA.USER_FRIENDS where USER_ID = ?";

        return jdbcTemplate.query(sql, this::mapRowToUser, user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select * from SCHEMA.USER_FILMORATE";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        String sqlQuery = "select ID, EMAIL, LOGIN, BIRTHDAY,NAME " +
                "from SCHEMA.USER_FILMORATE " +
                "where ID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id));
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
    public User updateUser(User user) {
        log.info("Попытка обновить пользователя id: {}", user.getId());
        String sqlQuery = "UPDATE SCHEMA.USER_FILMORATE SET " +
                "EMAIL = ?, LOGIN = ?, BIRTHDAY = ?, NAME = ? " +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getName()
                , user.getId());
        log.info("Пользователь обновлён. id: {}", user.getId());
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .login(resultSet.getString("LOGIN"))
                .email(resultSet.getString("EMAIL"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    private Map<String, Object> UserToMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", user.getEmail());
        values.put("LOGIN", user.getLogin());
        values.put("BIRTHDAY", user.getBirthday());
        values.put("NAME", user.getName());
        return values;
    }
}
