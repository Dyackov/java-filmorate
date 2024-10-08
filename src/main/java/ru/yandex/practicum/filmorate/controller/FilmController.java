package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmServiceImpl;

    /**
     * GET — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано,
     * возвращает первые 10.
     */
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") long count,
                                      @RequestParam(required = false) Integer genreId,
                                      @RequestParam(required = false) Integer year) {
        log.info("Запрос на получение популярных фильмов по лайкам. count: {}, genreId: {}, year: {}", count, genreId, year);
        return filmServiceImpl.getPopularFilms(count, genreId, year);
    }

    /**
     * DELETE - удаление фильма по идентификатору.
     */
    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFilmById(@PathVariable long filmId) {
        log.info("Запрос на удаление фильма. ID фильма:{}.", filmId);
        filmServiceImpl.deleteFilmById(filmId);
    }

    /**
     * DELETE - удаление всех фильмов.
     */
    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllFilms() {
        log.info("Запрос на удаление всех фильмов.");
        filmServiceImpl.deleteAllFilms();
    }


    /**
     * GET - вывод общих с другом фильмов с сортировкой по их популярности.
     */
    @GetMapping("/common")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) {
        log.info("Запрос на список общих фильмов. ID пользователя1:{} , ID пользователя2:{} ", userId, friendId);
        return filmServiceImpl.getCommonFilms(userId, friendId);
    }


    /**
     * PUT - пользователь ставит лайк фильму.
     */
    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeToFilm(@PathVariable("id") long filmId, @PathVariable("userId") long userId) {
        log.info("Запрос на добавление лайка фильму с ID: {} , от пользователя c ID: {}.", filmId, userId);
        filmServiceImpl.addLikeToFilm(filmId, userId);
    }

    /**
     * DELETE - пользователь удаляет лайк.
     */
    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLikeFromFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.info("Запрос на удаление лайка.");
        filmServiceImpl.removeLikeFromFilm(filmId, userId);
    }

    /**
     * POST — создание фильма.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Запрос на создание фильма {}", film);
        return filmServiceImpl.createFilm(film);
    }

    /**
     * PUT — обновление фильма.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody Film newFilm) {
        log.info("Запрос на обновление фильма {}", newFilm);
        return filmServiceImpl.updateFilm(newFilm);
    }

    /**
     * GET — получение всех фильмов.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов.");
        return filmServiceImpl.getAllFilms();
    }

    /**
     * GET — получение фильма по ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable long id) {
        log.info("Запрос на получение фильма по ID: {}.", id);
        return filmServiceImpl.getFilmById(id);
    }
}
