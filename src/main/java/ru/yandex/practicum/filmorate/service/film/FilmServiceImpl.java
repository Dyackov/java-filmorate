package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.service.event.EventServiceImpl;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage jdbcFilmRepository;
    private final UserServiceImpl userServiceImpl;
    private final EventServiceImpl eventServiceImpl;

    @Autowired
    public FilmServiceImpl(FilmStorage jdbcFilmRepository, UserServiceImpl userServiceImpl, EventServiceImpl eventServiceImpl) {
        this.jdbcFilmRepository = jdbcFilmRepository;
        this.userServiceImpl = userServiceImpl;
        this.eventServiceImpl = eventServiceImpl;
    }

    /**
     * Получение популярных фильмов.
     */
    @Override
    public List<Film> getPopularFilms(long count, Integer genreId, Integer year) {
/*//        Optional<Long> limitCount = Optional.ofNullable(count);
//        return getAllFilms().stream()
//                .sorted((f1, f2) -> Integer.compare(f2.getIdUserLike().size(), f1.getIdUserLike().size()))
//                .filter(film -> genreId == null || film.getGenres().contains(genreServiceImpl.getGenreById(genreId)))
//                .filter(film -> year == null || film.getReleaseDate().getYear() == year)
//                .limit(limitCount.orElse(DEFAULT_POPULAR_FILMS_COUNT))
//                .peek(film -> log.info("film - {}", film))
//                .toList();*/
        return jdbcFilmRepository.getPopularFilms(genreId, year).stream().limit(count).toList();
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        userServiceImpl.getUserById(userId);
        userServiceImpl.getUserById(friendId);
        return jdbcFilmRepository.getCommonFilms(userId, friendId);
    }

    /**
     * Создание фильма.
     */
    @Override
    public Film createFilm(Film film) {
        Validator.validateFilm(film);
        return jdbcFilmRepository.createFilm(film);
    }

    /**
     * Обновление фильма.
     */
    @Override
    public Film updateFilm(Film newFilm) {
        jdbcFilmRepository.getFilmById(newFilm.getId());
        Validator.validateFilm(newFilm);
        return jdbcFilmRepository.updateFilm(newFilm);
    }

    /**
     * Получение фильма по ID.
     */
    @Override
    public Film getFilmById(long id) {
        return jdbcFilmRepository.getFilmById(id);
    }

    /**
     * Получение всех фильмов.
     */
    @Override
    public List<Film> getAllFilms() {
        return jdbcFilmRepository.getAllFilms();
    }

    /**
     * Удаление фильма по ID.
     */
    @Override
    public void deleteFilmById(long filmId) {
        getFilmById(filmId);
        jdbcFilmRepository.deleteFilmById(filmId);
    }

    /**
     * Удаление всех фильмов.
     */
    @Override
    public void deleteAllFilms() {
        jdbcFilmRepository.deleteAllFilms();
    }

    /**
     * Добавление лайка к фильму.
     */
    @Override
    public void addLikeToFilm(long filmId, long userId) {
        eventServiceImpl.createEvent(userId, EventType.LIKE, Operation.ADD,filmId);
        jdbcFilmRepository.addLikeToFilm(filmId, userId);
    }

    /**
     * Удаление лайка.
     */
    @Override
    public void removeLikeFromFilm(long filmId, long userId) {
        jdbcFilmRepository.getFilmById(filmId);
        userServiceImpl.getUserById(userId);
        jdbcFilmRepository.removeLikeFromFilm(filmId, userId);
        eventServiceImpl.createEvent(userId, EventType.LIKE, Operation.REMOVE,filmId);
    }

//    /**
//     * Получение популярных фильмов.
//     */
//    @Override
//    public List<Film> getPopularFilms(Integer count) {
////        Optional<Long> optionalCount = Optional.ofNullable(count);
//        log.info("Получение популярных фильмов.");
//        return jdbcFilmRepository.getAllFilms().stream()
//                .sorted((film1, film2) -> Integer.compare(film2.getIdUserLike().size(), film1.getIdUserLike().size()))
////                .limit(optionalCount.orElse(DEFAULT_POPULAR_FILMS_COUNT))
//                .toList();
//    }
}
