package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();


    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            films.replace(film.getId(), film);
        }
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidateExeption("Имя не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidateExeption("Описание не может превышать 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateExeption("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            throw new ValidateExeption("Продолжительность фильма не может быть отрицательной.");
        }
    }

}
