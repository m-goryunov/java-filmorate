package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.util.SqlMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final SqlMapper map;
    private final GenreStorage genreStorage;

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate, SqlMapper map, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.map = map;
        this.genreStorage = genreStorage;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        try {
            String sqlQuery = "SELECT FILM.ID, FILM.NAME, DESCRIPTION, DURATION, RELEASE_DATE," +
                    " FILM.RATING_ID, r.RATING_NAME " +
                    "FROM FILM " +
                    "JOIN RATING AS r ON r.RATING_ID = FILM.RATING_ID " +
                    "WHERE FILM.ID = ?";
            Film film = jdbcTemplate.queryForObject(sqlQuery, map::mapRowToFilm, id);
            assert film != null;
            film.setGenres(genreStorage.getFilmGenre(film.getId()));

            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Фильм не существует." + e.getMessage());
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT FILM.ID, FILM.NAME, DESCRIPTION, DURATION, RELEASE_DATE, FILM.RATING_ID, r.RATING_NAME " +
                "FROM FILM " +
                "JOIN RATING AS r ON r.RATING_ID = FILM.RATING_ID ";
        List<Film> films = jdbcTemplate.query(sqlQuery, map::mapRowToFilm);
        genreStorage.setFilmGenre(films);
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO FILM(RATING_ID, NAME, DESCRIPTION, DURATION, RELEASE_DATE) " +
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
        if (film.getGenres() != null) {
            genreStorage.setFilmGenre(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE FILM SET " +
                "RATING_ID = ?, NAME = ?, DESCRIPTION = ?, DURATION = ?, RELEASE_DATE = ?" +
                "WHERE ID = ?";
        String deleteSqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        int rows = jdbcTemplate.update(sqlQuery,
                film.getMpa().getId(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getId());
        if (rows == 0) {
            throw new EntityNotFoundException("Обновление несуществующего фильма.", getClass().toString());
        }
        if (film.getGenres() == null) {
            jdbcTemplate.update(deleteSqlQuery, film.getId());
        } else {
            jdbcTemplate.update(deleteSqlQuery, film.getId());
            genreStorage.setFilmGenre(film.getId(), film.getGenres());
            film.setGenres(genreStorage.getFilmGenre(film.getId()));
        }
        return film;
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        String sqlQuery = "SELECT FILM.ID, FILM.NAME, DESCRIPTION, DURATION, RELEASE_DATE, FILM.RATING_ID," +
                "r.RATING_NAME AS RATING_NAME, LIKES.LIKES " +
                "FROM FILM" +
                " LEFT JOIN (SELECT FILM_ID, COUNT(*) AS LIKES" +
                " FROM FILM_LIKES " +
                " GROUP BY FILM_ID) LIKES " +
                " ON LIKES.FILM_ID = FILM.ID " +
                " JOIN RATING AS r ON r.RATING_ID = FILM.RATING_ID " +
                " ORDER BY LIKES DESC LIMIT ?;";

        List<Film> films = jdbcTemplate.query(sqlQuery, map::mapRowToFilm, count);
        genreStorage.setFilmGenre(films);
        return films;
    }

}
