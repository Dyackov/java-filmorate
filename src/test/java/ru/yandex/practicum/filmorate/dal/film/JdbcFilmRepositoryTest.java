package ru.yandex.practicum.filmorate.dal.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.JdbcFilmRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmRepositoryTest {
    private final JdbcFilmRepository jdbcFilmRepository;

    @Test
    @DirtiesContext
    public void testCreateFilm() {
        Mpa mpa = new Mpa(1, "G");
        Genre genre = new Genre(1, "мультфильм");
        Film film = new Film();
        film.setName("d238UnHaKXkD2bO");
        film.setDescription("7BN9CpzdAluQkMxzfDFpkII");
        film.setReleaseDate(LocalDate.of(1969, 3, 5));
        film.setDuration(175);
        film.setMpa(mpa);
        film.setGenres(Set.of(genre));
        jdbcFilmRepository.createFilm(film);

        assertThat(jdbcFilmRepository.getFilmById(film.getId()).getName()).isEqualTo("d238UnHaKXkD2bO");
        assertThat(jdbcFilmRepository.getFilmById(film.getId()).getDescription()).isEqualTo("7BN9CpzdAluQkMxzfDFpkII");
        assertThat(jdbcFilmRepository.getFilmById(film.getId()).getDuration()).isEqualTo(175);
        assertThat(jdbcFilmRepository.getFilmById(film.getId()).getMpa()).isEqualTo(mpa);
    }

    @Test
    @DirtiesContext
    public void testFindFilmById() {
        Film film = new Film();
        Mpa mpa = new Mpa(1, "G");
        Genre genre = new Genre(1, "мультфильм");
        film.setName("d238UnHaKXkD2bO");
        film.setDescription("7BN9CpzdAluQkMxzfDFpkII");
        film.setReleaseDate(LocalDate.of(1969, 3, 5));
        film.setDuration(175);
        film.setMpa(mpa);
        film.setGenres(Set.of(genre));
        jdbcFilmRepository.createFilm(film);

        Optional<Film> filmOptional = Optional.of(jdbcFilmRepository.getFilmById(1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(filmTest ->
                        assertThat(filmTest).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    @DirtiesContext
    public void testUpdateFilm() {
        Film film = new Film();
        Mpa mpa = new Mpa(1, "G");
        Genre genre = new Genre(1, "мультфильм");
        film.setName("d238UnHaKXkD2bO");
        film.setDescription("7BN9CpzdAluQkMxzfDFpkII");
        film.setReleaseDate(LocalDate.of(1969, 3, 5));
        film.setDuration(175);
        film.setMpa(mpa);
        film.setGenres(Set.of(genre));
        jdbcFilmRepository.createFilm(film);

        Film newFilm = new Film();
        Mpa newMpa = new Mpa(1, "G");
        Genre newGenre = new Genre(3, "драмма");
        newFilm.setId(1L);
        newFilm.setName("dadfO");
        newFilm.setDescription("JFH123hO");
        newFilm.setReleaseDate(LocalDate.of(1999, 12, 17));
        newFilm.setDuration(120);
        newFilm.setMpa(newMpa);
        newFilm.setGenres(Set.of(newGenre));
        jdbcFilmRepository.updateFilm(newFilm);

        assertThat(jdbcFilmRepository.getFilmById(newFilm.getId()).getName()).isEqualTo("dadfO");
        assertThat(jdbcFilmRepository.getFilmById(newFilm.getId()).getDescription()).isEqualTo("JFH123hO");
        assertThat(jdbcFilmRepository.getFilmById(newFilm.getId()).getDuration()).isEqualTo(120);
        assertThat(jdbcFilmRepository.getFilmById(newFilm.getId()).getMpa()).isEqualTo(mpa);
    }
}
