package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

@Slf4j
public class Validator {
    private static final LocalDate DATE_OF_RELEASE = LocalDate.of(1895, 12, 28);

    public static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(DATE_OF_RELEASE)) {
            log.warn("Не пройдена проверка даты релиза.");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }

        if (film.getDuration().toMinutes() < Duration.ZERO.toMinutes()) {
            log.warn("Не пройдена проверка на продолжительность.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }

    public static void validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Не пройдена проверка даты рождения.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Указали пустое поле имени, будет использован логин - {} .", user.getLogin());
        }
    }
}
