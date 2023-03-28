package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FilmService {
    private ValidateService validateFilm = new ValidateService();
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;


    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    public Film addFilm(Film film) {
        validateFilm.validateFilm(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Фильм " + film.getId() + " добавлен.");

        return film;
    }

    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм " + film.getId() + " обновлён.");
        } else {
            throw new ValidateExeption("Обновление несуществующего пользователя.");
        }
        return film;
    }
}
