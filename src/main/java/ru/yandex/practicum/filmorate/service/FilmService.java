package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmService {


    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getMostPopularFilms(Integer count);

    Film getFilmById(Integer id);

    Genre getGenre(Integer id);

    List<Genre> getFilmGenre(Integer id);

    List<Genre> getAllFilmGenre();

    Rating getRating(Integer id);

    List<Rating> getAllRatings();
}
