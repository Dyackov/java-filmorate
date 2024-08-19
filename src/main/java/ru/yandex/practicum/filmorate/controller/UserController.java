package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * PUT - добавление в друзья.
     */
    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriends(@PathVariable("id") long userId, @PathVariable long friendId) {
        log.info("Получен запрос на добавление в друзья ID: {}, от пользователя ID {}.", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    /**
     * DELETE - удаление из друзей.
     */
    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("Получен запрос на удаление из друзей.");
        userService.removeFriend(userId, friendId);
    }

    /**
     * GET - возвращаем список пользователей, являющихся его друзьями.
     */
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") long userId) {
        log.info("Получен запрос на получение друзей пользователя с ID: {}.", userId);
        return userService.getFriends(userId);
    }

    /**
     * GET - список друзей, общих с другим пользователем.
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable("id") long userId, @PathVariable long otherId) {
        log.info("Получен запрос на получение общих друзей пользователя ID: {}, с пользователем ID: {}.", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }

    /**
     * DELETE - удаление всех пользоваетелей.
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllUsers() {
        log.info("Получен запрос на удаление всех пользователей");
        userService.deleteAllUsers();
    }

    /**
     * DELETE - удаление пользоваетеля по ID.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable long userId) {
        log.info("Получен запрос на удаление пользователя с ID: {}", userId);
        userService.deleteByIdUser(userId);
    }

    /**
     * GET - получение пользователя во ID.
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable long userId) {
        log.info("Получен запрос на получение пользователя с ID: {}.", userId);
        return userService.findUserById(userId);
    }

    /**
     * GET - получение всех пользователей.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        log.info("Получен запрос на получение всех пользователей.");
        return userService.findAll();
    }

    /**
     * PUT - обновление пользователя.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody User newUser) {
        log.info("Получен запрос на обновление пользователя {}.", newUser);
        return userService.update(newUser);
    }

    /**
     * POST - создание пользователя.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя {}.", user);
        return userService.create(user);
    }
}
