package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Getter
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        Validator.validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Создан новый фильм - {} , ID - {}.", film.getName(), film.getId());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("При обновлении данных не указали ID.");
            throw new ValidationException("ID должен быть указан.");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.warn("В базе нет фильма с ID - {}.", newFilm.getId());
            throw new NotFoundException("Фильм с ID = " + newFilm.getId() + " не найден");
        }
        Validator.validateFilm(newFilm);
        films.put(newFilm.getId(), newFilm);
        log.debug("Информация о фильме - {} , ID - {} , обновлена.", newFilm.getName(), newFilm.getId());
        return newFilm;
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Выполняется GET запрос на получение всех Фильмов.");
        return new ArrayList<>(films.values());
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public void clearFilms() {
        films.clear();
    }
}
