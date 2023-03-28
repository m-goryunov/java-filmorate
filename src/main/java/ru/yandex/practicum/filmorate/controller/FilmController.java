package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {

    FilmService service = new FilmService();

    @GetMapping("/films")
    List<Film> getAllFilms(){return service.getAllFilms();}

    @PostMapping(value = "/film")
    Film addFilm(@RequestBody Film film){
        return service.addFilm(film);
    }

}
