package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.GenreStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.LikeStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.RatingStorageImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
        @Sql(scripts = {"classpath:add_test_objects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = {"classpath:remove_test_objects.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class FilmDaoTest {

    private final FilmStorageImpl filmStorage;
    private final LikeStorageImpl likeStorage;
    private final GenreStorageImpl genreStorage;
    private final RatingStorageImpl ratingStorage;

    Rating testRating = Rating.builder()
            .id(1)
            .name("G")
            .build();

    Film testFilm = Film.builder()
            .id(0)
            .name("Default Name")
            .description("Default description")
            .duration(120)
            .releaseDate(LocalDate.of(2005, Month.APRIL, 12))
            .mpa(testRating)
            .genres(new ArrayList<>())
            .build();
    User testUser = User.builder()
            .id(0)
            .email("mail@mail.com")
            .login("testUser")
            .name(null)
            .birthday(LocalDate.of(1985, Month.AUGUST, 21))
            .build();

    @Test
    void getFilmById() {
        Film film = filmStorage.getFilmById(0);
        assertThat(Optional.of(film))
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("id", 0)
                                .hasFieldOrPropertyWithValue("name", "Default Name")
                                .hasFieldOrPropertyWithValue("description", "Default description")
                                .hasFieldOrPropertyWithValue("duration", 120L)
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2005, Month.APRIL, 12))
                                .hasFieldOrPropertyWithValue("mpa", testRating
                                )
                );
    }

    @Test
    void getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        Assertions.assertEquals(1, films.size());
        assertThat(Optional.ofNullable(films.get(0)))
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("id", 0)
                                .hasFieldOrPropertyWithValue("name", "Default Name")
                                .hasFieldOrPropertyWithValue("description", "Default description")
                                .hasFieldOrPropertyWithValue("duration", 120L)
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2005, Month.APRIL, 12))
                                .hasFieldOrPropertyWithValue("mpa", testRating)
                );
    }

    @Test
    void createFilm() {
        Film wrongFilm = Film.builder()
                .id(1)
                .build();

        assertThrows(NullPointerException.class, () -> filmStorage.createFilm(wrongFilm));
    }

    @Test
    void updateFilm() {
        Rating updatedRating = Rating.builder()
                .id(1)
                .name("G")
                .build();

        Film updatedFilm = Film.builder()
                .id(0)
                .name("Updated Name")
                .description("Updated description")
                .duration(122L)
                .releaseDate(LocalDate.of(2006, Month.APRIL, 12))
                .mpa(updatedRating)
                .build();

        filmStorage.updateFilm(updatedFilm);
        Film testFilm = filmStorage.getFilmById(updatedFilm.getId());

        assertThat(Optional.of(testFilm))
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("id", 0)
                                .hasFieldOrPropertyWithValue("name", "Updated Name")
                                .hasFieldOrPropertyWithValue("description", "Updated description")
                                .hasFieldOrPropertyWithValue("duration", 122L)
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2006, Month.APRIL, 12))
                                .hasFieldOrPropertyWithValue("mpa", updatedRating)
                );
    }

    @Test
    void addLike() {
        likeStorage.addLike(testFilm, testUser);
    }

    @Test
    void deleteLike() {
        likeStorage.addLike(testFilm, testUser);
        likeStorage.deleteLike(testFilm, testUser);
    }

    @Test
    void getMostPopularFilms() {
        List<Film> films = filmStorage.getMostPopularFilms(1);
        Assertions.assertEquals(1, films.size());
        assertThat(Optional.ofNullable(films.get(0)))
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("id", 0)
                                .hasFieldOrPropertyWithValue("name", "Default Name")
                                .hasFieldOrPropertyWithValue("description", "Default description")
                                .hasFieldOrPropertyWithValue("duration", 120L)
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2005, Month.APRIL, 12))
                                .hasFieldOrPropertyWithValue("mpa", testRating)
                );
    }

    @Test
    void getGenreById() {
        Genre genre = genreStorage.getGenreById(1);
        Assertions.assertEquals(Genre.builder().id(1).name("Комедия").build(), genre);
    }

    @Test
    void getFilmGenre() {
        genreStorage.getFilmGenre(0);
    }

    @Test
    void getAllFilmGenre() {
        List<Genre> genres = genreStorage.getAllFilmGenre();
        Assertions.assertEquals(6, genres.size());
        Assertions.assertEquals(genres.get(0), Genre.builder().id(1).name("Комедия").build());
    }

    @Test
    void getRating() {
        Rating rating = ratingStorage.getRating(1);
        Assertions.assertEquals(rating, Rating.builder().id(1).name("G").build());
    }

    @Test
    void getAllRatings() {
        List<Rating> ratings = ratingStorage.getAllRatings();
        Assertions.assertEquals(5, ratings.size());
        Assertions.assertEquals(ratings.get(0), Rating.builder().id(1).name("G").build());
    }

}
