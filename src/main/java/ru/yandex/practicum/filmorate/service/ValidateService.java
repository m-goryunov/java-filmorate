package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
@Slf4j
public class ValidateService {

    public void validateFilm(Film film) {

        if (film.getName().isEmpty()) {
            throw new ValidateExeption("Имя не может быть пустым.");
        }
        log.debug("Фильм: " + film.getId() + " Прошел проверку 1");
        if (film.getDescription().length() > 200) {
            throw new ValidateExeption("Описание не может превышать 200 символов.");
        }
        log.debug("Фильм: " + film.getId() + " Прошел проверку 2");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateExeption("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
        log.debug("Фильм: " + film.getId() + " Прошел проверку 3");
        if (film.getDuration() < 0) {
            throw new ValidateExeption("Продолжительность фильма не может быть отрицательной.");
        }
        log.debug("Фильм: " + film.getId() + " Прошел проверку 4");
    }

    public void validateUser(User user) {

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("Пользователь: " + user.getId() + ". Замена имени на логин.");
        }

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidateExeption("Email не может быть пустым и должен содержать @.");
        }
        log.debug("Пользователь: " + user.getId() + " Прошел проверку 1");
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidateExeption("Логин не может содержать пробелы и быть пустым.");
        }
        log.debug("Пользователь: " + user.getId() + " Прошел проверку 2");

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateExeption("Дата рождения не может быть в будущем");
        }
        log.debug("Пользователь: " + user.getId() + " Прошел проверку 3");
    }

}
