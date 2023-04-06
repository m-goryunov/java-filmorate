package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InMemoryFilmService implements FilmService {

    private final FilmStorage storage;
    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmService(FilmStorage storage, UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    @Override
    public void addLike(Film film, User user) {
        if (userStorage.getUserById(user.getId()) == null || storage.getFilmById(film.getId()) == null) {
            throw new UserNotFoundException("Фильм/Пользователь не найден.");
        }
        film.addLike(user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        film.removeLike(user.getId());
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return storage.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLikesCount))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getMostPopularFilms() {
        return storage.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLikesCount))
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(Integer id) {
        return storage.getFilmById(id);
    }

}
