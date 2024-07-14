package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {

    FilmStorage filmStorage = new InMemoryFilmStorage();

    @BeforeEach
    public void clearFilms() {
        filmStorage.clearFilms();
    }

    @Test
    public void createTest() {
        Film film = new Film(1L, "Название", "Описание фильма",
                LocalDate.of(2010, 6, 6), Duration.ofMinutes(120),new HashSet<>());

        filmStorage.createFilm(film);

        assertEquals(film, filmStorage.findAllFilms().getFirst());
    }

    @Test
    public void update() {
        Film oldFilm = new Film(1L, "Название", "Описание фильма",
                LocalDate.of(2010, 6, 6), Duration.ofMinutes(120),new HashSet<>());
        filmStorage.createFilm(oldFilm);

        Film newFilm = new Film(1L, "Новое название", "Новое описание фильма",
                LocalDate.of(2015, 6, 6), Duration.ofMinutes(80),new HashSet<>());

        filmStorage.updateFilm(newFilm);

        assertEquals(newFilm, filmStorage.findAllFilms().getFirst());
    }

    @Test
    public void findAll() {
        Film film1 = new Film(1L, "Название 1", "Описание фильма 1",
                LocalDate.of(2010, 6, 6), Duration.ofMinutes(120),new HashSet<>());
        Film film2 = new Film(2L, "Название 2", "Описание фильма 2",
                LocalDate.of(2015, 7, 5), Duration.ofMinutes(80),new HashSet<>());
        Film film3 = new Film(3L, "Название 3", "Описание фильма 3",
                LocalDate.of(2020, 8, 4), Duration.ofMinutes(90),new HashSet<>());

        Collection<Film> films = List.of(film1, film2, film3);

        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        filmStorage.createFilm(film3);

        assertEquals(films, filmStorage.findAllFilms());
    }
}
