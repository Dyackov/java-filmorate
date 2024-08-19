package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void removeLikeFromFilm(long filmId, long userId);

    void addLikeToFilm(long filmId, long userId);

    void deleteAllFilms();

    void deleteFilmById(long id);

    Film updateFilm(Film newFilm);

    Film createFilm(Film film);

    Film getFilmById(long id);

    List<Film> getAllFilms();
}
