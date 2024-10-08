package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private Long id;
    @NotNull(message = "Название фильма не может быть пустым.")
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов.")
    private String description;
    private LocalDate releaseDate;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Integer duration;
    private Set<Long> idUserLike = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;
}
