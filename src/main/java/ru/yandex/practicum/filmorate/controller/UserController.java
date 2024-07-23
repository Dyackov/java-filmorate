package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

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
        userService.addFriend(userId, friendId);
    }

    /**
     * DELETE - удаление из друзей.
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable("id") long userId, @PathVariable long friendId) {
        userService.removeFriend(userId, friendId);
    }

    /**
     * GET - возвращаем список пользователей, являющихся его друзьями.
     */
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") long userId) {
        return userService.getFriends(userId);
    }

    /**
     * GET - список друзей, общих с другим пользователем.
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable("id") long userId, @PathVariable long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    /**
     * POST - создание пользователя.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    /**
     * PUT - обновление пользователя.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    /**
     * GET - получение всех пользователей.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return userService.findAll();
    }

    /**
     * GET - получение пользователя во ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

}
