package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreRepository;

    public Genre getGenreById(int id) {
        return genreRepository.getGenreById(id);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    public Set<Genre> getAllGenresForFilm(long filmId) {
        return genreRepository.getAllGenresByFilmId(filmId);
    }
}
