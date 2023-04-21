package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.util.SqlMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final SqlMapper map;
    private final NamedParameterJdbcTemplate namedParam;

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate, SqlMapper map, NamedParameterJdbcTemplate namedParam) {
        this.jdbcTemplate = jdbcTemplate;
        this.map = map;
        this.namedParam = namedParam;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        try {
            String sqlQuery = "SELECT FILM.ID, FILM.NAME, DESCRIPTION, DURATION, RELEASE_DATE, RATING_ID, r.NAME " +
                    "FROM SCHEMA.FILM " +
                    "JOIN SCHEMA.RATING AS r ON r.ID = FILM.RATING_ID " +
                    "WHERE FILM.ID = ?";
            Film film = jdbcTemplate.queryForObject(sqlQuery, map::mapRowToFilm, id);
            assert film != null;
            film.setGenre(getFilmGenre(film.getId()));

            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Фильм не существует." + e.getMessage());
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT FILM.ID, FILM.NAME, DESCRIPTION, DURATION, RELEASE_DATE, RATING_ID, r.NAME " +
                "FROM SCHEMA.FILM " +
                "JOIN SCHEMA.RATING AS r ON r.ID = FILM.RATING_ID ";
        List<Film> films = jdbcTemplate.query(sqlQuery, map::mapRowToFilm);
        setFilmGenre(films);
        return films; // ??
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO SCHEMA.FILM(RATING_ID, NAME, DESCRIPTION, DURATION, RELEASE_DATE) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setInt(1, film.getMpa().getId());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setLong(4, film.getDuration());
            stmt.setDate(5, Date.valueOf(film.getReleaseDate()));
            return stmt;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        if (film.getGenre() != null) {
            setFilmGenre(film.getId(), film.getGenre());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE SCHEMA.FILM SET " +
                "NAME = ?, DESCRIPTION = ?, DURATION = ?, RELEASE_DATE = ?, RATING_ID = ?" +
                "WHERE id = ?";
        String deleteSqlQuery = "DELETE FROM SCHEMA.FILM_GENRE WHERE FILM_ID = ?";
        int rows = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getDuration(),
                film.getReleaseDate(), film.getMpa().getId(), film.getId());
        if (rows == 0) {
            throw new EntityNotFoundException("Обновление несуществующего фильма.", getClass().toString());
        }
        if (film.getGenre() == null) {
            jdbcTemplate.update(deleteSqlQuery, film.getId());
        } else {
            jdbcTemplate.update(deleteSqlQuery, film.getId());
            setFilmGenre(film.getId(), film.getGenre());
            film.setGenre(getFilmGenre(film.getId()));
        }
        return film;
    }

    @Override
    public void addLike(Film film, User user) {

    }

    @Override
    public void deleteLike(Film film, User user) {

    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        String sqlQuery = "SELECT FILM.ID, FILM.NAME, DESCRIPTION, DURATION, RELEASE_DATE, RATING_ID," +
                "r.NAME AS RATING_NAME, LIKES.LIKES " +
                "FROM SCHEMA.FILM" +
                " LEFT JOIN (SELECT FILM_ID, COUNT(*) AS LIKES" +
                " FROM SCHEMA.FILM_LIKES " +
                " GROUP BY FILM_ID) LIKES " +
                " ON LIKES.FILM_ID = FILM.ID " +
                " JOIN SCHEMA.RATING AS r ON r.ID = FILM.RATING_ID " +
                " ORDER BY LIKES DESC LIMIT ?;";

        List<Film> films = jdbcTemplate.query(sqlQuery, map::mapRowToFilm, count);
        setFilmGenre(films);
        return films;
    }

    @Override
    public Optional<Genre> getGenre(Integer id) {
        String sqlQuery = "SELECT ID, NAME FROM SCHEMA.GENRE WHERE ID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, map::mapRowToGenre, id));
    }

    @Override
    public List<Genre> getFilmGenre(Integer id) {
        String sqlQuery = "SELECT ID, NAME " +
                "FROM SCHEMA.GENRE " +
                "WHERE ID IN (" +
                "SELECT GENRE_ID " +
                "FROM SCHEMA.FILM_GENRE " +
                "WHERE FILM_ID = ?" +
                ")";
        return jdbcTemplate.query(sqlQuery, map::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAllFilmGenre() {
        String sqlQuery = "SELECT ID, NAME FROM SCHEMA.GENRE";
        return jdbcTemplate.query(sqlQuery, map::mapRowToGenre);
    }

    @Override
    public Optional<Rating> getRating(Integer id) {
        String sqlQuery = "SELECT ID, NAME FROM SCHEMA.RATING WHERE ID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, map::mapRowToRating, id));
    }

    @Override
    public List<Rating> getAllRatings() {
        String sqlQuery = "SELECT ID, NAME FROM SCHEMA.RATING";
        return jdbcTemplate.query(sqlQuery, map::mapRowToRating);
    }

    private void setFilmGenre(List<Film> films) {
        String sqlQuery =
                "SELECT FILM_ID, g.ID AS genre_id, g.NAME " +
                        "FROM SCHEMA.FILM_GENRE AS fg " +
                        "JOIN SCHEMA.GENRE g ON g.ID = fg.GENRE_ID " +
                        "WHERE FILM_ID IN (:ids)";

        Set<Integer> ids = films.stream().map(Film::getId).collect(Collectors.toSet());
        SqlParameterSource p = new MapSqlParameterSource("ids", ids);
        namedParam.query(sqlQuery, p, (rs, rn) -> map.mapRowToFilmsWithGenres(rs, rn, films));
    }

    private void setFilmGenre(Integer filmId, List<Genre> genre) {
        String sql = "INSERT INTO SCHEMA.FILM_GENRE (FILM_ID, GENRE_ID) " +
                "VALUES (?, ?)";
        try {
            jdbcTemplate.batchUpdate(sql, genre, 2,
                    (ps, p) -> {
                        ps.setInt(1, filmId);
                        ps.setInt(2, p.getId());
                    });
        } catch (DuplicateKeyException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

}
