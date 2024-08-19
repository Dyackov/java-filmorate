package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    /**
     * PUT - пользователь ставит лайк фильму.
     */
    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeToFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.info("Получен запрос на добавление лайка фильму с ID: {} , от пользователя c ID: {}.", filmId, userId);
        filmService.addLikeToFilm(filmId, userId);
    }

    /**
     * DELETE - пользователь удаляет лайк.
     */
    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLikeFromFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.info("Получен запрос на удаление лайка.");
        filmService.removeLikeFromFilm(filmId, userId);
    }

    /**
     * GET — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано,
     * возвращает первые 10.
     */
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(required = false) @Positive Integer count) {
        log.info("Получен запрос на получение популярных фильмов.");
        return filmService.getPopularFilms(count);
    }

    /**
     * POST — создание фильма.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на создание фильма {}.", film);
        return filmService.createFilm(film);
    }

    /**
     * PUT — обновление фильма.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма {}.", newFilm);
        return filmService.updateFilm(newFilm);
    }

    /**
     * GET — получение всех фильмов.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> findAllFilms() {
        log.info("Получен запрос на получение всех фильмов.");
        return filmService.findAllFilms();
    }

    /**
     * GET — получение фильма по ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable long id) {
        log.info("Получен запрос на получение фильма по ID: {}.", id);
        return filmService.getFilmById(id);
    }
}
