package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.storage.util.SqlMapper;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class RatingStorageImpl implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlMapper map;


    @Autowired
    public RatingStorageImpl(JdbcTemplate jdbcTemplate, SqlMapper map) {
        this.jdbcTemplate = jdbcTemplate;
        this.map = map;
    }

    @Override
    public Rating getRating(Integer id) {
        try {
            String sqlQuery = "SELECT RATING_ID, RATING_NAME FROM RATING WHERE RATING_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, map::mapRowToRating, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Указанный рейтинг отсутствует." + e.getMessage());
        }
    }

    @Override
    public List<Rating> getAllRatings() {
        String sqlQuery = "SELECT RATING_ID, RATING_NAME FROM RATING";
        return jdbcTemplate.query(sqlQuery, map::mapRowToRating);
    }
}
