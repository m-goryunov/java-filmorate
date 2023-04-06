package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.util.ValidateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final ValidateService validateFilm;
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    @Override
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        validateFilm.validateFilm(film);
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
            throw new UserNotFoundException("Обновление несуществующего фильма.");
        }
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        if (films.get(id) == null) {
            throw new UserNotFoundException("Фильм не существует");
        }

        return films.get(id);
    }
}
