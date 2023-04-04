package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class InMemoryFilmService implements FilmService {

    /*Будет отвечать за операции с фильмами, — добавление и удаление лайка, вывод 10 наиболее популярных фильмов по количеству лайков.
    Пусть пока каждый пользователь может поставить лайк фильму только один раз.*/

    private FilmStorage filmStorage;

    @Autowired
    public InMemoryFilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Film film, User user) {
        film.addLike(user.getId());
    }

    public void removeLike(Film film, User user) {
        film.removeLike(user.getId());
    }

    public Set<Integer> getMostPopularFilms() {

        List<Film> films = filmStorage.getAllFilms();

        return null;
    }

}
