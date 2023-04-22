package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@Primary
@Slf4j
public class DbFilmService implements FilmService {

    FilmStorage storage;

    public DbFilmService(@Qualifier("dbFilmStorage") FilmStorage storage) {
        this.storage = storage;
    }


    @Override
    public Film getFilmById(Integer id) {
        return storage.getFilmById(id).orElseThrow(() ->
                new EntityNotFoundException("Фильм/Пользователь не найден.", getClass().toString()));
    }

    @Override
    public List<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    @Override
    public Film createFilm(Film film) {
        return storage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    @Override
    public void addLike(Film film, User user) {
        storage.addLike(film, user);
    }

    @Override
    public void deleteLike(Film film, User user) {
        storage.deleteLike(film, user);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return storage.getMostPopularFilms(count);
    }

    @Override
    public Genre getGenreById(Integer id) {
        return storage.getGenreById(id);
    }

    @Override
    public List<Genre> getFilmGenre(Integer id) {
        return storage.getFilmGenre(id);
    }

    @Override
    public List<Genre> getAllFilmGenre() {
        return storage.getAllFilmGenre();
    }

    @Override
    public Rating getRating(Integer id) {
        return storage.getRating(id);
    }

    @Override
    public List<Rating> getAllRatings() {
        return storage.getAllRatings();
    }
}
