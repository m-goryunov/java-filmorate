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
