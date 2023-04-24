package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.util.SqlMapper;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class FriendStorageImpl implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlMapper mapper;

    @Autowired
    public FriendStorageImpl(JdbcTemplate jdbcTemplate, SqlMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public void addFriend(User user, User otherUser) {
        String sqlQuery = "INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, user.getId(), otherUser.getId());
    }

    @Override
    public void removeFriend(User user, User otherUser) {
        String sqlQuery = "DELETE FROM USER_FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        int rows = jdbcTemplate.update(sqlQuery, user.getId(), otherUser.getId());
        if (rows == 0) {
            throw new NoSuchElementException("Друг отсутствует.");
        }
    }

    @Override
    public List<User> getFriendsList(User user) {
        String sql = "SELECT ID, NAME, LOGIN, EMAIL, BIRTHDAY " +
                "FROM USER_FILMORATE " +
                "WHERE ID IN" +
                "(SELECT FRIEND_ID FROM USER_FRIENDS WHERE USER_ID = ?)";

        return jdbcTemplate.query(sql, mapper::mapRowToUser, user.getId());
    }

    @Override
    public List<User> getMutualFriends(User user, User friend) {
        String sqlQuery = "SELECT * FROM USER_FILMORATE u, USER_FRIENDS f, USER_FRIENDS o " +
                "         WHERE u.USER_ID = f.FRIEND_ID " +
                "         AND u.USER_ID = o.FRIEND_ID " +
                "         AND f.USER_ID = ? AND o.USER_ID = ?";

        return jdbcTemplate.query(sqlQuery, mapper::mapRowToUser, user.getId(), friend.getId());
    }
}
