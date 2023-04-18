package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.util.ValidateService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class FilmorateApplicationTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validationUserServiceTest() {

        ValidateService validateService = new ValidateService();
        User user = new User();
        user.setName("Name");
        user.setLogin("kraken");
        user.setId(1);
        user.setBirthday(LocalDate.now().minusYears(2));
        user.setEmail("");
        user.setBirthday(LocalDate.now().plusDays(5));

        Set<ConstraintViolation<User>> userViolations = validator.validate(user);
        assertFalse(userViolations.isEmpty());

        Film film = new Film();
        film.setId(1);
        film.setName("");
        film.setDescription("ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn" +
                "_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn" +
                "_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn" +
                "_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn" +
                "_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn_ghbdtn");

        film.setReleaseDate(LocalDate.of(1892, 12, 28));
        Assertions.assertThrows(ValidateException.class, () -> validateService.validateFilm(film), "Дата релиза ранее заданной.");
        film.setDuration(-2000);

        Set<ConstraintViolation<Film>> filmViolations = validator.validate(film);
        assertFalse(filmViolations.isEmpty());
    }
}
