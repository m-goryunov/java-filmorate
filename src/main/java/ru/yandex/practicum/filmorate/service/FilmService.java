package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmService {


    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getMostPopularFilms(Integer count);

    List<Film> getMostPopularFilms();

    Film getFilmById(Integer id);
}
