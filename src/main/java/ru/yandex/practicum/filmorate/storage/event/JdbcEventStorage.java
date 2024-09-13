package ru.yandex.practicum.filmorate.storage.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
@Slf4j
public class JdbcEventStorage extends BaseRepository<Event> implements EventStorage{

    public JdbcEventStorage(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    private final String CREATE_EVENT_QUERY = """
            INSERT INTO events (TIMESTAMP,USER_ID,EVENT_TYPE,OPERATION,ENTITY_ID)
            VALUES (?, ?, ?, ?, ?)
            """;

    private final String GET_EVENT_BY_USER_ID_QUERY = """
            SELECT *
            FROM events
            WHERE user_id = ?
            """;


    @Override
    public void createEvent(Event event) {
        long id = insert(CREATE_EVENT_QUERY,
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType().toString(),
                event.getOperation().toString(),
                event.getEntityId());
        event.setEventId(id);
        log.info("Event сохранён в базе данных.\n{}.", event);
    }

    @Override
    public List<Event> getEvenByUserId(long userId) {
        log.info("Получение ленты событий из базы данных. Пользователь ID: {}.",userId);
        return findMany(GET_EVENT_BY_USER_ID_QUERY, userId);
    }
}
