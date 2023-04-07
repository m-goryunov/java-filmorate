package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @GetMapping("/films")
    List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PostMapping(value = "/films")
    Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping(value = "/films")
    Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(filmService.getFilmById(id), userService.getUserById(userId));
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(filmService.getFilmById(id), userService.getUserById(userId));
    }

    @Validated
    @GetMapping("/films/popular")
    List<Film> getMostPopularFilmsCount(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("count передан из контроллера{}", count);
        return filmService.getMostPopularFilms(count);
    }
}
