package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaRaringService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Validator implements ApplicationContextAware {
    private static ApplicationContext context;
    private static final LocalDate DATE_OF_RELEASE = LocalDate.of(1895, 12, 28);
    private static final int ZERO = 0;

    public static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(DATE_OF_RELEASE)) {
            log.warn("Не пройдена проверка даты релиза.");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() < ZERO) {
            log.warn("Не пройдена проверка на продолжительность.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }

        GenreService genreService = context.getBean(GenreService.class);
        List<Genre> genres = genreService.getAllGenres();
        List<Long> genresId = new ArrayList<>();
        for (Genre genre : genres) {
            genresId.add(genre.getId());
        }

        List<Genre> genresFilm = new ArrayList<>(film.getGenres());
        List<Long> genresIdFilm = new ArrayList<>();
        for (Genre genre : genresFilm) {
            genresIdFilm.add(genre.getId());
        }

        for (Long id : genresIdFilm) {
            if (!genresId.contains(id)) {
                throw new ValidationException("Неверный ID жанра.");
            }
        }

        MpaRaringService mpaRaringService = context.getBean(MpaRaringService.class);

        List<Mpa> maps = mpaRaringService.getAll();
        List<Integer> pasId = new ArrayList<>();
        for (Mpa mpa : maps) {
            pasId.add(mpa.getId());
        }

        if (!pasId.contains(film.getMpa().getId())) {
            throw new ValidationException("Неверный ID рейтинга.");
        }

    }

    public static void validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Не пройдена проверка даты рождения.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Указали пустое поле имени, будет использован логин - {} .", user.getLogin());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
