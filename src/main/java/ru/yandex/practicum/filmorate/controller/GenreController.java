package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    /**
     * GET - получение жанра по ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre getGenreById(@PathVariable int id) {
        log.info("Получен запрос на получение жанра по ID: {}.", id);
        return genreService.getGenreById(id);
    }

    /**
     * GET - получение всех жанров.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> findAll() {
        log.info("Получен запрос на получение всех жанров.");
        return genreService.getAllGenres();
    }
}
