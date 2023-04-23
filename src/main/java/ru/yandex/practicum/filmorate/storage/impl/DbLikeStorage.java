package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class DbLikeStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbLikeStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Film film, User user) {
        try {
            String sqlQuery = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID_LIKE) " +
                    "VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    film.getId(),
                    user.getId());
        } catch (DataIntegrityViolationException e) {
            throw new NoSuchElementException("Ошибка при добавлении лайка. " + e.getMessage());
        }
    }

    @Override
    public void deleteLike(Film film, User user) {
        String sqlQuery = "DELETE FROM FILM_LIKES " +
                "WHERE USER_ID_LIKE = ? AND FILM_ID = ?";
        int rows = jdbcTemplate.update(sqlQuery,
                user.getId(),
                film.getId());
        if (rows == 0) {
            throw new NoSuchElementException("Отсутствует лайк для удаления.");
        }
    }

}
