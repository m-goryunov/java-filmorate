package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.util.ValidateService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;
    private final ValidateService validateFilm;

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
        validateFilm.validateFilm(film);
        return filmService.createFilm(film);
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

    @GetMapping("/films/popular")
    List<Film> getMostPopularFilmsCount(@Positive @NotNull @RequestParam(defaultValue = "10") Integer count) {
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping("/genres")
    List<Genre> getAllGenres() {
        return filmService.getAllFilmGenre();
    }

    @GetMapping("/genres/{id}")
    List<Genre> getGenreById(@Positive @NotNull @PathVariable Integer id) {
        return filmService.getFilmGenre(id);
    }

    @GetMapping("/mpa")
    List<Rating> getAllRatings() {
        return filmService.getAllRatings();
    }

    @GetMapping("/mpa/{id}")
    Rating getRatingById(@Positive @NotNull @PathVariable Integer id) {
        return filmService.getRating(id);
    }


}

