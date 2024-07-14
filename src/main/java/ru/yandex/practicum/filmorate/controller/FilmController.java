package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

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
    public void likeFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
        filmService.likeFilm(filmId, userId);
    }

    /**
     * DELETE - пользователь удаляет лайк.
     */
    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLikeFromFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
        filmService.removeLikeFromFilm(filmId, userId);
    }

    /**
     * GET — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано,
     * возвращает первые 10.
     */
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(required = false) @Positive Integer count) {
        return filmService.getPopularFilms(count);
    }

    /**
     * POST — создание фильма.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    /**
     * PUT — обновление фильма.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    /**
     * GET — получение всех фильмов.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    /**
     * GET — получение фильма по ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }
}
