package ru.yandex.practicum.filmorate.dal.mpa_rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.util.List;

@Slf4j
@Repository
public class MpaRatingRepository extends BaseRepository<Mpa> implements MpaRatingStorage {

    public MpaRatingRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    private final static String FIND_ALL_QUERY = "SELECT * from mpa_rating";
    private final static String FIND_NAME_BY_ID_QUERY = "SELECT * FROM mpa_rating WHERE rating_id = ?";

    @Override
    public List<Mpa> getAllMpas() {
        log.info("Получение всех рейтингов из базы данных.");
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Mpa getMpaById(int id) {
        log.info("Получение рейтинга по ID из базы данных RatingID: {}.", id);
        Mpa mpa = findOne(FIND_NAME_BY_ID_QUERY, id).orElseThrow(() -> {
            String errorMessage = "Рейтинга с ID - " + id + " не существует";
            log.warn("Ошибка получения по ID {}: {}", id, errorMessage);
            return new NotFoundException(errorMessage);
        });
        log.info("Получен рейтинга по ID из базы данных RatingID: {}.", id);
        return mpa;
    }
}
