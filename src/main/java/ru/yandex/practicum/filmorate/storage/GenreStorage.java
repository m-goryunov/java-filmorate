package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenreById(Integer id);

    List<Genre> getFilmGenre(Integer id);

    List<Genre> getAllFilmGenre();

    void setFilmGenre(List<Film> films);

    void setFilmGenre(Integer filmId, List<Genre> genre);
}
