package ru.practicum.shareit.errors;

public class ObjectFoundException extends RuntimeException {
    public ObjectFoundException(String message) {
        super(message);
    }
}