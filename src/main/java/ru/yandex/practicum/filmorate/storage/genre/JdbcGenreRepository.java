package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Repository
public class JdbcGenreRepository extends BaseRepository<Genre> implements GenreStorage {

    public JdbcGenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_ALL_GENRE_QUERY = "SELECT * from genres";
    private static final String FIND_NAME_BY_ID_GENRE_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String FIND_All_GENRES_FOR_FILM_QUERY = """
            SELECT g.*
            FROM genres g
            INNER JOIN films_genres fg ON g.genre_id = fg.genre_id
            WHERE film_id = ?
            """;

    /**
     * Получение всех жанров конкретного фильма.
     */
    @Override
    public List<Genre> getAllGenresByFilmId(long filmId) {
        log.info("Получение всех жанров фильма из базы данных FilmID: {}.", filmId);
        return findMany(FIND_All_GENRES_FOR_FILM_QUERY, filmId)/*.stream().sorted(Comparator.comparingLong(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new))*/;
    }

    /**
     * Получение жанра по ID.
     */
    @Override
    public Genre getGenreById(int id) {
        log.info("Получение жанра по ID из базы данных GenreID: {}.", id);
        Genre genre = findOne(FIND_NAME_BY_ID_GENRE_QUERY, id).orElseThrow(() -> {
            String errorMessage = "Жанра с ID - " + id + " не существует.";
            log.warn("Ошибка получения жанра по ID {}: {}", id, errorMessage);
            return new NotFoundException(errorMessage);
        });
        log.info("Получен жанр по ID из базы данных GenreID: {}.", id);
        return genre;
    }

    /**
     * Получение всех жанров.
     */
    @Override
    public List<Genre> getAllGenres() {
        log.info("Получение всех жанров из базы данных.");
        return findMany(FIND_ALL_GENRE_QUERY);
    }
}
