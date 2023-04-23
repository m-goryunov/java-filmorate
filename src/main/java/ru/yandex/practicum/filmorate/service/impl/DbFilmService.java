package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Service
@Slf4j
public class DbFilmService implements FilmService {

    private final FilmStorage storage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;

    public DbFilmService(FilmStorage storage, LikeStorage likeStorage, GenreStorage genreStorage, RatingStorage ratingStorage) {
        this.storage = storage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
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
        likeStorage.addLike(film, user);
    }

    @Override
    public void deleteLike(Film film, User user) {
        likeStorage.deleteLike(film, user);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return storage.getMostPopularFilms(count);
    }

    @Override
    public Genre getGenreById(Integer id) {
        return genreStorage.getGenreById(id);
    }

    @Override
    public List<Genre> getFilmGenre(Integer id) {
        return genreStorage.getFilmGenre(id);
    }

    @Override
    public List<Genre> getAllFilmGenre() {
        return genreStorage.getAllFilmGenre();
    }

    @Override
    public Rating getRating(Integer id) {
        return ratingStorage.getRating(id);
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingStorage.getAllRatings();
    }
}
