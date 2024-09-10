package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.event.EventServiceImpl;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userServiceImpl;
    private  final EventServiceImpl eventServiceImpl;

    @GetMapping("{id}/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getUserFeed(@PathVariable long id){
        log.info("Запрос на получение ленты событий. ID пользователя: {}.", id);
        return eventServiceImpl.getEvenByUserId(id);
    }


    /**
     * POST - создание пользователя.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user) {
        log.info("Запрос на создание пользователя: \n{}", user);
        return userServiceImpl.createUser(user);
    }

    /**
     * PUT - обновление пользователя.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User user) {
        log.info("Запрос на обновление пользователя:\n{}", user);
        return userServiceImpl.updateUser(user);
    }

    /**
     * PUT - добавление в друзья.
     */
    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addUserFriend(@PathVariable("id") long userId, @PathVariable long friendId) {
        log.info("Запрос на добавление в друзья. ID пользователя 1: {}, ID пользователя 2: {}.", userId, friendId);
        userServiceImpl.addFriend(userId, friendId);
    }

    /**
     * GET - получение пользователя во ID.
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable long userId) {
        log.info("Запрос на получение пользователя. ID пользователя: {}.", userId);
        return userServiceImpl.getUserById(userId);
    }

    /**
     * GET - получение всех пользователей.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей.");
        return userServiceImpl.getAllUsers();
    }

    /**
     * GET - возвращаем список пользователей, являющихся его друзьями.
     */
    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") long userId) {
        log.info("Запрос на получение друзей пользователя. ID пользователя: {}.", userId);
        return userServiceImpl.getUserFriends(userId);
    }

    /**
     * GET - список друзей, общих с другим пользователем.
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable("id") long userId, @PathVariable long otherId) {
        log.info("Запрос на получение общих друзей пользователя ID: {}, с пользователем ID: {}.", userId, otherId);
        return userServiceImpl.getCommonFriends(userId, otherId);
    }

    /**
     * DELETE - удаление из друзей.
     */
    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("Запрос на удаление из друзей. ID пользователя 1: {}, ID пользователя 2: {}.", userId, friendId);
        userServiceImpl.removeFriend(userId, friendId);
    }

    /**
     * DELETE - удаление пользоваетеля по ID.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable long userId) {
        log.info("Запрос на удаление пользователя с ID: {}", userId);
        userServiceImpl.deleteUserById(userId);
    }

    /**
     * DELETE - удаление всех пользоваетелей.
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllUsers() {
        log.info("Запрос на удаление всех пользователей");
        userServiceImpl.deleteAllUsers();
    }

}
