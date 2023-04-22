package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(Integer id);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getMostPopularFilms(Integer count);

    Genre getGenreById(Integer id);

    List<Genre> getFilmGenre(Integer id);

    List<Genre> getAllFilmGenre();

    Rating getRating(Integer id);

    List<Rating> getAllRatings();
}
