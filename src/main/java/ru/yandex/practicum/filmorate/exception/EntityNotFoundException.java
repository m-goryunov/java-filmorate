package ru.yandex.practicum.filmorate.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, String s) {
        super(message);
    }
}