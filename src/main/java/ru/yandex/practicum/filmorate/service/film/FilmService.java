package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    /**
     * Создание фильма.
     */
    Film createFilm(Film film);

    /**
     * Обновление фильма.
     */
    Film updateFilm(Film newFilm);

    /**
     * Получение фильма по ID.
     */
    Film getFilmById(long id);

    /**
     * Получение всех фильмов.
     */
    List<Film> getAllFilms();

    /**
     * Удаление фильма по ID.
     */
    void deleteFilmById(long id);

    /**
     * Удаление всех фильмов.
     */
    void deleteAllFilms();

    /**
     * Добавление лайка к фильму.
     */
    void addLikeToFilm(long filmId, long userId);

    /**
     * Удаление лайка.
     */
    void removeLikeFromFilm(long filmId, long userId);

    /**
     * Получение популярных фильмов.
     */
    List<Film> getPopularFilms(Integer count);

}
