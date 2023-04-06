package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

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

    @GetMapping("/films/popular?count={count}")
    List<Film> getMostPopularFilmsCount(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping("/films/popular")
    List<Film> getMostPopularFilms() {
        return filmService.getMostPopularFilms();
    }

}
