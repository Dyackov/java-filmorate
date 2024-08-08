package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmRepository;
    private final UserService userService;
    private static final int DEFAULT_POPULAR_FILMS_COUNT = 10;

    @Autowired
    public FilmService(FilmStorage filmRepository, UserService userService) {
        this.filmRepository = filmRepository;
        this.userService = userService;
    }

    /**
     * Добавление лайка к фильму.
     */
    public void addLikeToFilm(long filmId, long userId) {
        filmRepository.addLikeToFilm(filmId, userId);
    }

    /**
     * Удаление лайка.
     */
    public void removeLikeFromFilm(long filmId, long userId) {
        filmRepository.getFilmById(filmId);
        userService.findUserById(userId);
        filmRepository.removeLikeFromFilm(filmId, userId);
    }

    /**
     * Получение популярных фильмов.
     */
    public List<Film> getPopularFilms(Integer count) {
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        log.info("Получение популярных фильмов.");
        return filmRepository.getAllFilms().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getIdUserLike().size(), film1.getIdUserLike().size()))
                .limit(optionalCount.orElse(DEFAULT_POPULAR_FILMS_COUNT))
                .toList();
    }

    /**
     * Получение фильма по ID.
     */
    public Film getFilmById(long id) {
        return filmRepository.getFilmById(id);
    }

    /**
     * Создание фильма.
     */
    public Film createFilm(Film film) {
        return filmRepository.createFilm(film);
    }

    /**
     * Обновление фильма.
     */
    public Film updateFilm(Film newFilm) {
        return filmRepository.updateFilm(newFilm);
    }

    /**
     * Получение всех фильмов.
     */
    public List<Film> findAllFilms() {
        return filmRepository.getAllFilms();
    }
}
