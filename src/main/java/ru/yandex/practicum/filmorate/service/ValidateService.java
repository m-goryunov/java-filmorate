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
    private static final LocalDate VALID_DATE = LocalDate.of(1895, 12, 28);

    public void validateFilm(Film film) {

        if (film.getReleaseDate().isBefore(VALID_DATE)) {
            throw new ValidateExeption("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
        log.debug("Фильм прошел проверку на дату", film.getId());
    }

    public void validateUser(User user) {

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("Замена имени на логин.", user.getId());
        }
    }

}
