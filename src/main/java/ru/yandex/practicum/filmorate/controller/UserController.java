package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.*;

@Getter
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        Validator.validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Создан новый пользователь - {} , ID - {}.", user.getLogin(), user.getId());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("При обновлении данных не указали ID.");
            throw new ValidationException("ID должен быть указан.");
        }
        if (users.get(newUser.getId()) == null) {
            log.warn("В базе нет пользователя с ID - {}.", newUser.getId());
            throw new NotFoundException("Пользователь с ID - " + newUser.getId() + " не найден");
        }
        Validator.validateUser(newUser);
        users.put(newUser.getId(), newUser);
        log.debug("Информация о пользователе - {} , ID - {} , обновлена.", newUser.getLogin(), newUser.getId());
        return newUser;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Выполняется GET запрос на получение всех Пользователей.");
        return new ArrayList<>(users.values());
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public void clearUsers() {
        users.clear();
    }
}
