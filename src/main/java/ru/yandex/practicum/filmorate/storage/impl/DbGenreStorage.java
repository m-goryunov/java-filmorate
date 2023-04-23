package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.util.SqlMapper;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SqlMapper map;
    private final NamedParameterJdbcTemplate namedParam;

    @Autowired
    public DbGenreStorage(JdbcTemplate jdbcTemplate, SqlMapper map, NamedParameterJdbcTemplate namedParam) {
        this.jdbcTemplate = jdbcTemplate;
        this.map = map;
        this.namedParam = namedParam;
    }

    @Override
    public Genre getGenreById(Integer id) {
        try {
            String sqlQuery = "SELECT ID, NAME FROM GENRE WHERE ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, map::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Жанр отсутствует." + e.getMessage());
        }
    }

    @Override
    public List<Genre> getFilmGenre(Integer id) {
        String sqlQuery = "SELECT ID, NAME " +
                "FROM GENRE " +
                "WHERE ID IN (" +
                "SELECT GENRE_ID " +
                "FROM FILM_GENRE " +
                "WHERE FILM_ID = ?" +
                ")";
        return jdbcTemplate.query(sqlQuery, map::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAllFilmGenre() {
        String sqlQuery = "SELECT ID, NAME FROM GENRE";
        return jdbcTemplate.query(sqlQuery, map::mapRowToGenre);
    }

    @Override
    public void setFilmGenre(List<Film> films) {
        String sqlQuery =
                "SELECT FILM_ID, g.ID AS genre_id, g.NAME " +
                        "FROM FILM_GENRE AS fg " +
                        "JOIN GENRE g ON g.ID = fg.GENRE_ID " +
                        "WHERE FILM_ID IN (:ids)";

        final Map<Integer, Film> ids = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        SqlParameterSource p = new MapSqlParameterSource("ids", ids.keySet());
        namedParam.query(sqlQuery, p, (rs, rn) -> map.mapRowToFilmsWithGenres(rs, rn, ids));
    }

    @Override
    public void setFilmGenre(Integer filmId, List<Genre> genre) {
        String sql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) " +
                "VALUES (?, ?)";
        try {
            jdbcTemplate.batchUpdate(sql, genre, genre.size(),
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
