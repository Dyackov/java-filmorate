package ru.yandex.practicum.filmorate.storage.film;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    /**
     * Создание фильма.
     */
    public Film createFilm(Film film) {
        Validator.validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Создан новый фильм - {} , ID - {}.", film.getName(), film.getId());
        return film;
    }

    /**
     * Обновление фильма.
     */
    public Film updateFilm(Film newFilm) {
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
        log.info("Информация о фильме - {} , ID - {} , обновлена.", newFilm.getName(), newFilm.getId());
        return newFilm;
    }

    /**
     * Получение всех фильмов.
     */
    public List<Film> findAllFilms() {
        log.info("Получение всех фильмов.");
        return new ArrayList<>(films.values());
    }

    /**
     * Получение фильма по ID.
     */
    public Film getFilmById(long id) {
        if (!films.containsKey(id)) {
            log.warn("При получении фильма указали неверный ID.");
            throw new NotFoundException("Фильма с ID = " + id + " не существует.");
        }
        log.info("Получение фильма по ID.");
        return films.get(id);
    }

    /**
     * Удаление всех фильмов.
     */
    public void deleteAllFilms() {
        films.clear();
    }

    /**
     * Генерация ID.
     */
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
