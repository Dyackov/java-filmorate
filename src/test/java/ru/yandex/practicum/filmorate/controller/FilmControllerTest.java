package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController = new FilmController();

    @BeforeEach
    public void clearFilms() {
        filmController.clearFilms();
    }

    @Test
    public void createTest() {
        Film film = new Film(1L, "Название", "Описание фильма",
                LocalDate.of(2010, 6, 6), Duration.ofMinutes(120));

        filmController.create(film);

        assertEquals(film, filmController.getFilms().get(1L));
    }

    @Test
    public void update() {
        Film oldFilm = new Film(1L, "Название", "Описание фильма",
                LocalDate.of(2010, 6, 6), Duration.ofMinutes(120));
        filmController.create(oldFilm);

        Film newFilm = new Film(1L, "Новое название", "Новое описание фильма",
                LocalDate.of(2015, 6, 6), Duration.ofMinutes(80));

        filmController.update(newFilm);

        assertEquals(newFilm, filmController.getFilms().get(1L));
    }

    @Test
    public void findAll() {
        Film film1 = new Film(1L, "Название 1", "Описание фильма 1",
                LocalDate.of(2010, 6, 6), Duration.ofMinutes(120));
        Film film2 = new Film(2L, "Название 2", "Описание фильма 2",
                LocalDate.of(2015, 7, 5), Duration.ofMinutes(80));
        Film film3 = new Film(3L, "Название 3", "Описание фильма 3",
                LocalDate.of(2020, 8, 4), Duration.ofMinutes(90));

        Collection<Film> films = List.of(film1, film2, film3);

        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);

        assertEquals(films, filmController.findAll());
    }
}