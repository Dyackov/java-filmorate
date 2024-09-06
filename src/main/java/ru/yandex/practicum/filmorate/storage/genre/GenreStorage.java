package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    List<Genre> getAllGenresByFilmId(long filmId);

    Genre getGenreById(int id);

    List<Genre> getAllGenres();
}
