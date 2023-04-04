package ru.yandex.practicum.filmorate.storage.util;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class LikesCountComparator implements Comparator<Film> {


    @Override
    public int compare(Film o1, Film o2) {
        return o1.getLikesCount().compareTo(o2.getLikesCount());
    }
}
