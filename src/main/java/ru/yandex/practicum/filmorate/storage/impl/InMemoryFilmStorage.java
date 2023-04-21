package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    @Override
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Фильм добавлен.{}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм обновлён.{}", film.getId());
        } else {
            throw new EntityNotFoundException("Обновление несуществующего фильма.", getClass().toString());
        }
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void addLike(Film film, User user) {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public void deleteLike(Film film, User user) {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public Genre getGenre(Integer id) {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public List<Genre> getFilmGenre(Integer id) {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public List<Genre> getAllFilmGenre() {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public Rating getRating(Integer id) {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }

    @Override
    public List<Rating> getAllRatings() {
        throw new UnsupportedOperationException("Реализация существует только при работе с БД");
    }
}
