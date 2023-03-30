package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final ValidateService validateFilm;
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;


    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    public Film addFilm(Film film) {
        validateFilm.validateFilm(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Фильм добавлен.", film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм обновлён.", film.getId());
        } else {
            throw new ValidateExeption("Обновление несуществующего пользователя.");
        }
        return film;
    }
}
