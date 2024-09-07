package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    /**
     * Вывод общих с другом фильмов с сортировкой по их популярности.
     */
     List<Film> getCommonFilms(long userId, long friendId);

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
    void deleteFilmById(long filmId);

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
