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
import java.util.Optional;

@Component
public class SqlMapper {
    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .duration(resultSet.getInt("DURATION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .rating(Rating.builder().id(resultSet.getInt("RATING_ID")).name(resultSet.getString("RATING_NAME")).build())
                .genre(new ArrayList<>())
                .build();
    }

    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }

    public List<Film> mapRowToFilmsWithGenres(ResultSet resultSet, int rowNum, List<Film> films) throws SQLException {
        Integer filmId = resultSet.getInt("FILM_ID");

        Optional<Film> film = films.stream()
                .filter(it -> it.getId().equals(filmId))
                .findFirst();

        if (film.isPresent()) {
            List<Genre> genres;
            if (film.get().getGenre() != null) {
                genres = film.get().getGenre();
            } else genres = new ArrayList<>();
            genres.add(Genre.builder().id(resultSet.getInt("GENRE_ID")).name(resultSet.getString("GENRE_NAME")).build());
            film.get().setGenre(genres);
        }
        return null;
    }

    public Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return Rating.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
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
