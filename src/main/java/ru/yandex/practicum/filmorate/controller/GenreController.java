package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class GenreController {
    private final FilmService filmService;

    @GetMapping("/genres")
    List<Genre> getAllGenres() {
        return filmService.getAllFilmGenre();
    }

    @GetMapping("/genres/{id}")
    Genre getGenreById(@Positive @PathVariable Integer id) {
        return filmService.getGenreById(id);
    }
}
