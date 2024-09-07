package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreServiceImpl;
import ru.yandex.practicum.filmorate.service.mpa.MpaRatingService;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.HashSet;
import java.util.List;

@Repository
@Slf4j
public class JdbcFilmRepository extends BaseRepository<Film> implements FilmStorage {
    private final MpaRatingService mpaRatingService;
    private final GenreServiceImpl genreServiceImpl;

    public JdbcFilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, MpaRatingService mpaRatingService,
                              GenreServiceImpl genreServiceImpl) {
        super(jdbc, mapper);
        this.mpaRatingService = mpaRatingService;
        this.genreServiceImpl = genreServiceImpl;
    }

    private static final String DELETE_LIKE_BY_FILM_ID_QUERY = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
    private static final String DELETE_ALL_FILMS_QUERY = "DELETE FROM films";
    private static final String DELETE_BY_ID_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String FIND_FILM_BY_ID_QUERY = "SELECT * FROM films WHERE film_id=?";
    private static final String FIND_ALL_FILM_QUERY = "SELECT * FROM films";
    private static final String FIND_LIKE_QUERY = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String ADD_LIKE_TO_FILM_QUERY = """
            INSERT INTO likes(user_id,film_id)
            VALUES (?,?)
            """;
    private static final String UPDATE_FILM_QUERY = """
            UPDATE films
            SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ?
            WHERE film_id = ?
            """;
    private static final String CREATE_FILM_QUERY = """
            INSERT INTO films (name,description,release_date,duration,rating_id)
            VALUES (?,?,?,?,?)
            """;
    private static final String INSERT_GENRES_ID_QUERY = """
            INSERT INTO films_genres (film_id,genre_id)
            VALUES (?,?)
            """;

    private static final String GET_COMMON_FILMS_QUERY = """
            SELECT f.*, COUNT (l.user_id) AS popularity
            FROM films f
            JOIN likes l ON f.film_id = l.film_id
            WHERE l.user_id IN (? , ?)
            GROUP BY f.film_id , f.name
            HAVING COUNT(DISTINCT l.user_id) = 2
            ORDER BY popularity DESC;
            """;


    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        log.info("Получение общих фильмов из базы данных.");
        List<Film> films = findMany(GET_COMMON_FILMS_QUERY, userId,friendId);
        films.forEach(this::setFilmDetails);
        return films;

    }

    /**
     * Удаление лайка.
     */
    @Override
    public void removeLikeFromFilm(long filmId, long userId) {
        getFilmById(filmId);
        delete(DELETE_LIKE_BY_FILM_ID_QUERY, userId, filmId);
        log.info("Удаление записи лайка из базы данных - Пользователь ID: {} , Фильм ID: {}.", userId, filmId);
    }

    /**
     * Добавление лайка к фильму.
     */
    @Override
    public void addLikeToFilm(long filmId, long userId) {
        getFilmById(filmId);
        add(ADD_LIKE_TO_FILM_QUERY, userId, filmId);
        log.info("Создание записи лайка в базе данных - Пользователь ID: {} , Фильм ID: {}.", userId, filmId);
    }

    /**
     * Удаление всех фильмов.
     */
    @Override
    public void deleteAllFilms() {
        deleteAll(DELETE_ALL_FILMS_QUERY);
        log.info("Удалены все фильмы из базы данных.");
    }

    /**
     * Удаление фильма по ID.
     */
    @Override
    public void deleteFilmById(long filmId) {
        log.info("Удаление фильма из базы данных ID: {}.", filmId);
        delete(DELETE_BY_ID_FILM_QUERY, filmId);
        log.info("Удалён фильм из базы данных ID: {}.", filmId);
    }

    /**
     * Обновление фильма.
     */
    @Override
    public Film updateFilm(Film newFilm) {
        log.info("Обновление записи фильма в базе данных: {}.", newFilm);
        update(UPDATE_FILM_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId());
        for (Genre genre : newFilm.getGenres()) {
            add(INSERT_GENRES_ID_QUERY, newFilm.getId(), genre.getId());
        }
        setFilmDetails(newFilm);
        log.info("Обновлёна запись о фильме в базе данных: {}.", newFilm);
        return newFilm;
    }

    /**
     * Создание фильма.
     */
    @Override
    public Film createFilm(Film film) {
        log.info("Создание записи фильма в базе данных: {}.", film);
        long id = insert(CREATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        for (Genre genre : film.getGenres()) {
            add(INSERT_GENRES_ID_QUERY, id, genre.getId());
        }
        setFilmDetails(film);
        log.info("Создана запись о фильме в базе данных: {}.", film);
        return film;
    }

    /**
     * Получение фильма по ID.
     */
    @Override
    public Film getFilmById(long id) {
        log.info("Получение фильма из базы данных ID: {}.", id);
        Film film = findOne(FIND_FILM_BY_ID_QUERY, id).orElseThrow(() -> {
            String errorMessage = "Фильма с ID - " + id + " не существует.";
            log.warn("Ошибка получения фильма по ID {}: {}", id, errorMessage);
            return new NotFoundException(errorMessage);
        });
        setFilmDetails(film);
        log.info("Получен фильм из базы данных ID: {}.", id);
        return film;
    }

    /**
     * Получение всех фильмов.
     */
    @Override
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов из базы данных.");
        List<Film> films = findMany(FIND_ALL_FILM_QUERY);
        if (films.isEmpty()) {
            throw new NotFoundException("Фильмы не найдены: база данных пуста.");
        }
        for (Film film : films) {
            setFilmDetails(film);
        }
        log.info("Получены все фильмы из базы данных.");
        return films;
    }

    /**
     * Присвоение жанра,рейтинга фильму,лайков.
     */
    private void setFilmDetails(Film film) {
        film.setMpa(mpaRatingService.getMpaById(film.getMpa().getId()));
        film.setGenres(genreServiceImpl.getAllGenresForFilm(film.getId()));
        film.setIdUserLike(new HashSet<>(findManyFriendsId(FIND_LIKE_QUERY, film.getId())));
    }
}
