package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InMemoryFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public void addLike(Film film, User user) {
        if (userStorage.getUserById(user.getId()) == null || filmStorage.getFilmById(film.getId()) == null) {
            throw new UserNotFoundException("Фильм/Пользователь не найден.");
        }
        film.addLike(user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        film.removeLike(user.getId());
    }

    @Override
    public List<Film> getMostPopularFilms(@Positive @NotNull Integer count) {
        log.info("count передан из сервиса{}", count);
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> {
                    int result = o1.getLikesCount().compareTo(o2.getLikesCount());
                    result = result * -1;
                    return result;
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getMostPopularFilms() {
        return getAllFilms().stream()
                .sorted((o1, o2) -> {
                    int result = o1.getLikesCount().compareTo(o2.getLikesCount());
                    result = result * -1;
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

}
