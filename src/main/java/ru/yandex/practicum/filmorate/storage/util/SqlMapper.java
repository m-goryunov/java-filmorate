package ru.yandex.practicum.filmorate.storage.util;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SqlMapper {
    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .duration(resultSet.getInt("DURATION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .mpa(Rating.builder().id(resultSet.getInt("RATING_ID")).name(resultSet.getString("RATING_NAME")).build())
                .genres(new ArrayList<>())
                .build();
    }

    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }

    public List<Film> mapRowToFilmsWithGenres(ResultSet resultSet, int rowNum, Map<Integer, Film> films) throws SQLException {
        Integer filmId = resultSet.getInt("FILM_ID");

        Film film = films.get(filmId);

        if (film != null) {
            List<Genre> genres;
            if (film.getGenres() != null) {
                genres = film.getGenres();
            } else genres = new ArrayList<>();
            genres.add(Genre.builder()
                    .id(resultSet.getInt("ID"))
                    .name(resultSet.getString("NAME"))
                    .build());
            film.setGenres(genres);
        }
        return null;
    }

    public Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return Rating.builder()
                .id(resultSet.getInt("RATING_ID"))
                .name(resultSet.getString("RATING_NAME"))
                .build();
    }

    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .login(resultSet.getString("LOGIN"))
                .email(resultSet.getString("EMAIL"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
