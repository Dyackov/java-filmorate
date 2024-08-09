package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    /**
     * Создание пользователя.
     */
    @Override
    public User createUser(User user) {
        Validator.validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь - {} , ID - {}.", user.getLogin(), user.getId());
        return user;
    }

    /**
     * Обновление пользователя.
     */
    @Override
    public User updateUser(User newUser) {
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
        log.info("Информация о пользователе - {} , ID - {} , обновлена.", newUser.getLogin(), newUser.getId());
        return newUser;
    }

    /**
     * Получение всех пользователей.
     */
    @Override
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей.");
        return new ArrayList<>(users.values());
    }

    /**
     * Получение пользователя по ID.
     */
    @Override
    public User getUserById(long userId) {
        if (!users.containsKey(userId)) {
            log.warn("При пользователя указали неверный ID.");
            throw new NotFoundException("Пользователя с ID = " + userId + " не существует.");
        }
        log.info("Получение пользователя по ID.");
        return users.get(userId);
    }

    /**
     * Удаление всех пользователей.
     */
    @Override
    public boolean removeAllUsers() {
        users.clear();
        return true;
    }

    @Override
    public void updateStatus(long userId, long friendId, String status) {

    }

    @Override
    public void removeFriend(long userId, long friendId) {

    }

    @Override
    public List<User> getAllFriends(long userId) {
        return List.of();
    }

    @Override
    public void addFriend(long userId, long friendId, String status) {

    }


    @Override
    public boolean removeFriendById(long userId) {
        return false;
    }

    /**
     * Генерация ID.
     */
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
