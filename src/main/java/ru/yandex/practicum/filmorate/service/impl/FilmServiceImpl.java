package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;

    public FilmServiceImpl(FilmStorage filmStorage, LikeStorage likeStorage, GenreStorage genreStorage, RatingStorage ratingStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
    }

    @Override
    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        genreStorage.setFilmGenre(List.of(film));
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        genreStorage.setFilmGenre(films);
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        Film film1 = filmStorage.createFilm(film);
        if (film1.getGenres() != null) {
            setFilmGenre(film1.getId(), film1.getGenres());
        }

        return film1;
    }

    @Override
    public Film updateFilm(Film film) {
        Film film1 = filmStorage.updateFilm(film);
        if (film1.getGenres() != null) {
            setFilmGenre(film.getId(), film.getGenres());
            film.setGenres(getFilmGenre(film.getId()));
        }
        return film1;
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
        List<Film> films = filmStorage.getMostPopularFilms(count);
        setFilmGenre(films);
        return films;
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

    @Override
    public void setFilmGenre(List<Film> films) {
        genreStorage.setFilmGenre(films);
    }

    @Override
    public void setFilmGenre(Integer filmId, List<Genre> genre) {
        genreStorage.setFilmGenre(filmId, genre);
    }
}
