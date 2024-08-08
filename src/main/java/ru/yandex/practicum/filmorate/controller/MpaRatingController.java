package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaRaringService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaRatingController {

    private final MpaRaringService mpaRaringService;

    /**
     * GET - получение рейтинга по ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa getMpaById(@PathVariable int id) {
        log.info("Получен запрос на получение реутинга по ID: {}.", id);
        return mpaRaringService.getMpaById(id);
    }

    /**
     * GET - получение всех рейтингов.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Mpa> findAll() {
        log.info("Получен запрос на получение всех рейтингов.");
        return mpaRaringService.getAll();
    }
}
