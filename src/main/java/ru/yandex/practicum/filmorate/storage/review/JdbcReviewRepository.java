package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcReviewRepository extends BaseRepository<Review> implements ReviewStorage {

    public JdbcReviewRepository(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    private static final String CREATE_REVIEW_QUERY = """
            INSERT INTO reviews (CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_REVIEW_QUERY = """
            UPDATE reviews
            SET CONTENT = ?, IS_POSITIVE = ?, USER_ID = ?, FILM_ID = ?, USEFUL = ?
            WHERE REVIEW_ID = ?
            """;

    private static final String DELETE_REVIEW_BY_ID_QUERY = """
            DELETE FROM reviews
            WHERE REVIEW_ID = ?
            """;

    private static final String GET_REVIEW_BY_ID_QUERY = """
            SELECT *
            FROM reviews
            WHERE REVIEW_ID = ?
            """;

    private static final String GET_REVIEWS_FILM_QUERY = """
            SELECT *
            FROM reviews
            WHERE ( ? IS NULL OR FILM_ID = ? )
            LIMIT ( ? IS NULL OR 10 )
            """;

    private static final String ADD_LIKE_OR_DISLIKE_REVIEW_QUERY = """
            INSERT INTO reviews_users ( REVIEW_ID, USER_ID ,IS_POSITIVE )
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE_LIKE_REVIEW_QUERY = """
            UPDATE reviews
            SET USEFUL = USEFUL + 1
            WHERE REVIEW_ID = ?
            """;

    private static final String UPDATE_DISLIKE_REVIEW_QUERY = """
            UPDATE reviews
            SET USEFUL = USEFUL - 1
            WHERE REVIEW_ID = ?
            """;

    private static final String GET_EXISTING_REVIEW_QUERY = """
            SELECT IS_POSITIVE
            FROM reviews_users
            WHERE REVIEW_ID = ? AND USER_ID = ?
            """;

    private static final String DELETE_LIKE_REVIEW_QUERY = """
            DELETE FROM reviews_users
            WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_POSITIVE = ?
            """;
    
    private static final String DELETE_DISLIKE_REVIEW_QUERY = """
            DELETE FROM reviews_users
            WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_POSITIVE = ?
            """;

    @Override
    public Review createReview(Review review) {
        long id = insert(CREATE_REVIEW_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful());
        review.setReviewId(id);
        log.debug("Отзыв сохранён в базе данных.\n{}", review);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        update(UPDATE_REVIEW_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful(),
                review.getReviewId());
        log.debug("Отзыв обновлён в базе данных.\n{}", review);
        return review;
    }

    @Override
    public void deleteReviewById(long reviewId) {
        delete(DELETE_REVIEW_BY_ID_QUERY, reviewId);
        log.debug("Отзыв удалён из базы данных. Отзыв ID: {}", reviewId);
    }

    @Override
    public Review getReviewById(long reviewId) {
        Review review = findOne(GET_REVIEW_BY_ID_QUERY, reviewId).orElseThrow(() -> {
            String errorMessage = "Отзыва с ID - " + reviewId + " не существует.";
            log.warn("Ошибка: отзыва по ID {}: {}", reviewId, errorMessage);
            return new NotFoundException(errorMessage);
        });
        log.debug("Получен отзыв из базы данных. ID отзыва: {}.", reviewId);
        return review;
    }

    @Override
    public List<Review> getReviewsFilm(Long filmId, Long count) {
        List<Review> reviews = findMany(GET_REVIEWS_FILM_QUERY, filmId, filmId, count);
        log.debug("Получены отзывы из базы данных. ID фильма: {}, Кол-во: {}", filmId, count);
        return reviews;
    }

    @Override
    public void addLikeReview(long id, long userId) {
        clearLikeDislike(id, userId);
        add(ADD_LIKE_OR_DISLIKE_REVIEW_QUERY, id, userId, true);
        log.debug("Добавлен лайк в базу данных. ID отзыва: {}, ID пользователя: {}", id, userId);
        update(UPDATE_LIKE_REVIEW_QUERY, id);
        log.debug("Рейтинг полезности отзыва обновлён в базе данных.( +1 )");
    }

    @Override
    public void addDisLikeReview(long id, long userId) {
        clearLikeDislike(id, userId);
        add(ADD_LIKE_OR_DISLIKE_REVIEW_QUERY, id, userId, false);
        log.debug("Добавлен дизлайк в базу данных. ID отзыва: {}, ID пользователя: {}", id, userId);
        update(UPDATE_DISLIKE_REVIEW_QUERY, id);
        log.debug("Рейтинг полезности отзыва обновлён в базе данных.( -1 )");
    }

    @Override
    public void deleteLikeReview(long id, long userId) {
        delete(DELETE_LIKE_REVIEW_QUERY, id, userId, true);
        log.debug("Удалён лайк из базы данных. ID отзыва: {}, ID пользователя: {}", id, userId);
        update(UPDATE_DISLIKE_REVIEW_QUERY, id);
        log.debug("Рейтинг полезности отзыва обновлён в базе данных.(-1)");
    }

    @Override
    public void deleteDisLikeReview(long id, long userId) {
        delete(DELETE_DISLIKE_REVIEW_QUERY, id, userId, false);
        log.debug("Удалён дизлайк из базы данных. ID отзыва: {}, ID пользователя: {}", id, userId);
        update(UPDATE_LIKE_REVIEW_QUERY, id);
        log.debug("Рейтинг полезности отзыва обновлён в базе данных.(+1)");
    }

    private void clearLikeDislike(long id, long userId) {
        Optional<Boolean> oldState = findOneBoolean(GET_EXISTING_REVIEW_QUERY, id, userId);
        if (oldState.isPresent()) {
            if (Boolean.TRUE.equals(oldState.get())) {
                delete(DELETE_LIKE_REVIEW_QUERY, id, userId, true);
                update(UPDATE_DISLIKE_REVIEW_QUERY, id);
            }
            if (Boolean.FALSE.equals(oldState.get())) {
                delete(DELETE_DISLIKE_REVIEW_QUERY, id, userId, false);
                update(UPDATE_LIKE_REVIEW_QUERY, id);
            }
        }
    }
}
