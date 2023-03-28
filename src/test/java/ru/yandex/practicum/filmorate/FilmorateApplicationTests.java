package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void validationServiceTest() {

        ValidateService validateService = new ValidateService();
        User user = new User();
        user.setName("Name");
        user.setId(1);
        user.setBirthday(LocalDate.now().minusYears(2));
        user.setLogin("kraken");
        user.setEmail("kitty@ya.ru");
        user.setEmail("");
        Assertions.assertThrows(ValidateExeption.class, () -> validateService.validateUser(user), "Пустой email");
        user.setEmail("kitty_sobaka_ya.ru");
        Assertions.assertThrows(ValidateExeption.class, () -> validateService.validateUser(user), "Не содержит sobaku");
        user.setEmail("kitty@ya.ru");
        user.setBirthday(LocalDate.now().plusDays(5));
        Assertions.assertThrows(ValidateExeption.class, () -> validateService.validateUser(user), "ДР в будущем");


        Film film = new Film();

        film.setId(1);
        film.setName("");
        Assertions.assertThrows(ValidateExeption.class, () -> validateService.validateFilm(film), "Пустое имя фильма.");
        film.setName("John Wick");
        film.setDescription("ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn" +
                "_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn" +
                "_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn" +
                "_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn" +
                "_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn");
        Assertions.assertThrows(ValidateExeption.class, () -> validateService.validateFilm(film), "Содержит более 200 символов");
        film.setDescription("");
        film.setReleaseDate(LocalDate.of(1892, 12, 28));
        Assertions.assertThrows(ValidateExeption.class, () -> validateService.validateFilm(film), "Дата релиза ранее заданной.");
        film.setDuration(-2000);
        Assertions.assertThrows(ValidateExeption.class, () -> validateService.validateFilm(film), "Отрицательная продолжительность.");
    }
}
