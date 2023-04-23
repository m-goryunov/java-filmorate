package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface LikeStorage {
    void addLike(Film film, User user);

    void deleteLike(Film film, User user);
}
