package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review createReview(@RequestBody @Valid Review review) {
        log.info("Запрос на создание отзыва:\n{}", review);
        return reviewServiceImpl.createReview(review);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Review updateReview(@RequestBody Review review) {
        log.info("Запрос на обновление отзыва. Новые данные:\n{}", review);
        return reviewServiceImpl.updateReview(review);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReview(@PathVariable long id) {
        log.info("Запрос на удаление отзыва. Отзыв ID: {}", id);
        reviewServiceImpl.deleteReviewById(id);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review getReview(@PathVariable long id) {
        log.info("Запрос на получение отзыва. Отзыв ID: {}", id);
        return reviewServiceImpl.getReviewById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Review> getReviewsFilm(@RequestParam(required = false) Long filmId,
                                       @RequestParam(required = false) Long count) {
        log.info("Запрос на получение всех отзывов по идентификатору фильма. Фильм ID: {}, Кол-во: {}", filmId, count);
        return reviewServiceImpl.getReviewsFilm(filmId, count);
    }

    @PutMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Запрос на добавление лайка отзыву. ID отзыва: {}, ID пользователя: {}",id,userId);
        reviewServiceImpl.addLikeReview(id,userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addDisLikeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Запрос на добавление дизлайка отзыву. ID отзыва: {}, ID пользователя: {}",id,userId);
        reviewServiceImpl.addDisLikeReview(id,userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLikeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Запрос на удаление лайка отзыву. ID отзыва: {}, ID пользователя: {}",id,userId);
        reviewServiceImpl.deleteLikeReview(id,userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDisLikeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Запрос на удаление дизлайка отзыву. ID отзыва: {}, ID пользователя: {}",id,userId);
        reviewServiceImpl.deleteDisLikeReview(id,userId);
    }
}
