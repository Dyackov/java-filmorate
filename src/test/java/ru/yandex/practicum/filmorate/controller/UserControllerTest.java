package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
    UserStorage userStorage = new InMemoryUserStorage();

    @BeforeEach
    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    @Test
    public void createTest() {
        User user = new User(1L, "1234fghj@yandex.ru", "qwerty", "Ivan",
                LocalDate.of(1900, 5, 15), new HashSet<>());

        userStorage.create(user);

        assertEquals(user, userStorage.findAll().getFirst());
    }

    @Test
    public void update() {
        User oldUser = new User(1L, "1234fghj@yandex.ru", "qwerty", "Ivan",
                LocalDate.of(1990, 5, 15), new HashSet<>());
        userStorage.create(oldUser);

        User newUser = new User(1L, "jjjjj@yandex.ru", "pojo", "Petr",
                LocalDate.of(1995, 12, 10), new HashSet<>());
        userStorage.update(newUser);

        assertEquals(newUser, userStorage.findAll().getFirst());
    }

    @Test
    public void findAll() {
        User user1 = new User(1L, "1234fghj@yandex.ru", "qwerty", "Ivan",
                LocalDate.of(1900, 5, 15), new HashSet<>());
        User user2 = new User(1L, "bogtor@yandex.ru", "tor", "Vasiliy",
                LocalDate.of(1900, 5, 15), new HashSet<>());
        User user3 = new User(1L, "Oleg123@yandex.ru", "oleg321", "Oleg",
                LocalDate.of(1900, 5, 15), new HashSet<>());

        Collection<User> users = List.of(user1, user2, user3);

        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);

        assertEquals(users, userStorage.findAll());
    }

}
