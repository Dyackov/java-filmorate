package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    @Email(message = "Электронная почта должна содержать символ @.")
    @NotNull(message = "Электронная почта не может быть пустой.")
    @NotBlank(message = "Электронная почта не может быть пустой.")
    private String email;
    @NotNull(message = "Логин не может быть пустым.")
    @NotBlank(message = "Логин не может содержать пробелы.")
    private String login;
    private String name;
    private LocalDate birthday;
}
