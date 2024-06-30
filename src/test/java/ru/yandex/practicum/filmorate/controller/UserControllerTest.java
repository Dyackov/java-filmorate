package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();

    @BeforeEach
    public void clearFilms() {
        userController.clearUsers();
    }

    @Test
    public void createTest() {
        User user = new User(1L, "1234fghj@yandex.ru", "qwerty", "Ivan",
                LocalDate.of(1900, 5, 15));

        userController.create(user);

        assertEquals(user, userController.getUsers().get(1L));
    }

    @Test
    public void update() {
        User oldUser = new User(1L, "1234fghj@yandex.ru", "qwerty", "Ivan",
                LocalDate.of(1990, 5, 15));
        userController.create(oldUser);

        User newUser = new User(1L, "jjjjj@yandex.ru", "pojo", "Petr",
                LocalDate.of(1995, 12, 10));
        userController.update(newUser);

        assertEquals(newUser, userController.getUsers().get(1L));
    }

    @Test
    public void findAll() {
        User user1 = new User(1L, "1234fghj@yandex.ru", "qwerty", "Ivan",
                LocalDate.of(1900, 5, 15));
        User user2 = new User(1L, "bogtor@yandex.ru", "tor", "Vasiliy",
                LocalDate.of(1900, 5, 15));
        User user3 = new User(1L, "Oleg123@yandex.ru", "oleg321", "Oleg",
                LocalDate.of(1900, 5, 15));

        Collection<User> users = List.of(user1, user2, user3);

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        assertEquals(users, userController.findAll());
    }

}
