package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        String sql = "insert into USER_FRIENDS(USER_ID, FRIEND_ID, FRIENDSHIP_STATUS)" +
                "VALUES (?,?,?)";
        jdbcTemplate.update(sql, user.getId(), otherUser.getId(), friendshipStatus);
    }

    @Override
    public void removeFriend(User user, User otherUser) {
        String sql = "delete from USER_FRIENDS " +
                "where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql,user.getId(),otherUser.getId());
    }


    @Override
    public boolean checkFriendship(User user, User otherUser) {
        String sqlQuery = "select FRIENDSHIP_STATUS " +
                "from USER_FRIENDS " +
                "where USER_ID = ? AND FRIEND_ID = ?";
        SqlRowSet response = jdbcTemplate.queryForRowSet(sqlQuery, user.getId(), otherUser.getId());

        return response.getBoolean("FRIENDSHIP_STATUS");
    }

    @Override
    public List<User> getFriendsList(User user) {
        String sql = "select * from USER_FRIENDS where USER_ID = ?";

        return jdbcTemplate.query(sql,this::mapRowToUser, user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select * from FILMORATE_USER";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        String sqlQuery = "select USER_ID,USER_EMAIL,USER_LOGIN,USER_BIRTHDAY,NAME " +
                "from FILMORATE_USER " +
                "where USER_ID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id));
    }

    @Override
    public User createUser(User user) {
        log.info("Попытка добавить пользователя");
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMORATE_USER")
                .usingGeneratedKeyColumns("USER_ID");
        log.info("Пользователь добавлен. id: {}", simpleJdbcInsert.executeAndReturnKey(UserToMap(user)));

/*        String sql = "insert into FILMORATE_USER(USER_EMAIL, USER_LOGIN, USER_BIRTHDAY)" +
                "VALUES (?,?,?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getBirthday());*/
        return user;

    }

    @Override
    public User updateUser(User user) {
        log.info("Попытка обновить пользователя id: {}", user.getId());
        String sqlQuery = "update FILMORATE_USER set " +
                "USER_EMAIL = ?, USER_LOGIN = ?, USER_BIRTHDAY = ?, NAME = ? " +
                "where USER_ID = ?";
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
                .id(resultSet.getInt("USER_ID"))
                .name(resultSet.getString("USER_NAME"))
                .login(resultSet.getString("USER_LOGIN"))
                .email(resultSet.getString("USER_EMAIL"))
                .birthday(resultSet.getDate("USER_BIRTHDAY").toLocalDate())
                .build();
    }

    private Map<String, Object> UserToMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("USER_EMAIL", user.getEmail());
        values.put("USER_LOGIN", user.getLogin());
        values.put("USER_BIRTHDAY", user.getBirthday());
        values.put("USER_NAME", user.getName());
        return values;
    }
}
