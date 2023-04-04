package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FilmController {

    /*
    PUT /films/{id}/like/{userId}  — пользователь ставит лайк фильму.
    DELETE /films/{id}/like/{userId}  — пользователь удаляет лайк.
    GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
                                       Если значение параметра count не задано, верните первые 10.*/

    private final InMemoryFilmStorage service;

    @GetMapping("/films")
    List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @PostMapping(value = "/films")
    Film addFilm(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping(value = "/films")
    Film updateFilm(@Valid @RequestBody Film film) {
        return service.updateFilm(film);
    }

}
