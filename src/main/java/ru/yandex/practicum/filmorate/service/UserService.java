package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NoUsersFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userRepository;
    private static final String CONFIRMED = "CONFIRMED";
    private static final String UNCONFIRMED = "UNCONFIRMED";


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Добавление в друзья.
     */
    public void addFriend(long userId, long friendId) {
        User user = findUserById(userId);
        User userFriend = findUserById(friendId);
        log.info("Пользователь {} добавляет в друзья {}.", user.getName(), userFriend.getName());

        String status = UNCONFIRMED;
        if (userFriend.getFriendsId().contains(userId)) {
            status = CONFIRMED;
            userRepository.updateStatus(friendId, userId, status);
        }
        userRepository.addFriend(userId, friendId, status);
        log.info("Пользователь {} добавил в друзья {}.", user.getName(), userFriend.getName());
    }

    /**
     * Возвращаем список пользователей, являющихся его друзьями.
     */
    public List<User> getFriends(long userId) {
        User user = userRepository.getUserById(userId);
        log.info("Получение списка всех друзей пользователя {}.", user.getName());
        return userRepository.getAllFriends(user.getId());
    }

    /**
     * Список друзей, общих с другим пользователем.
     */
    public List<User> getCommonFriends(long userId, long otherId) {
        User user = findUserById(userId);
        User userOther = findUserById(otherId);
        log.info("Получение списка общих друзей пользователя {} с пользователем {}.", user.getName(), userOther.getName());
        List<User> commonFriends = new ArrayList<>();
        for (Long id : user.getFriendsId()) {
            if (userOther.getFriendsId().contains(id)) {
                commonFriends.add(findUserById(id));
            }
        }
        log.info("Получен список общих друзей пользователя {} с пользователем {}.", user.getName(), userOther.getName());
        return commonFriends;
    }

    /**
     * Удаление из друзей.
     */
    public void removeFriend(long userId, long friendId) {
        log.info("Пользователь c ID: {} удаляет из друзей пользователя с ID: {}.", userId, friendId);
        userRepository.removeFriend(userId, friendId);
        log.info("Пользователь c ID: {} удалил из друзей пользователя с ID: {}.", userId, friendId);
    }

    /**
     * Удаление всех пользователей.
     */
    public void deleteAllUsers() {
        boolean deleted = userRepository.removeAllUsers();
        if (!deleted) {
            throw new NoUsersFoundException("Удаление невозможно. Пользователей не существует.");
        }
        log.info("Удаление всех пользователей.");
    }

    /**
     * Удаление пользователя по ID.
     */
    public void deleteByIdUser(long userId) {
        boolean deleted = userRepository.removeFriendById(userId);
        if (!deleted) {
            throw new NoUsersFoundException("Удаление невозможно. Пользователя с ID - " + userId + " не существует.");
        }
        log.info("Пользователь с ID - {} удалён.", userId);
    }

    /**
     * Получение пользователя по ID.
     */
    public User findUserById(long userId) {
        log.info("Получение пользователя c ID - {}.", userId);
        return userRepository.getUserById(userId);
    }

    /**
     * Получение всех пользователей.
     */
    public List<User> findAll() {
        log.info("Получение всех пользователей.");
        List<User> users = userRepository.getAllUsers();
        if (users.isEmpty()) {
            log.error("Список пользователей пуст.");
            throw new NoUsersFoundException("Список пользователей пуст.");
        }
        log.info("Все пользователи получены.");
        return users;
    }

    /**
     * Обновление пользователя.
     */
    public User update(User newUser) {
        log.info("Обновление информации пользователя - {}.", newUser);
        findUserById(newUser.getId());
        Validator.validateUser(newUser);
        log.info("Обновлена информация о пользователе - {}.", newUser);
        return userRepository.updateUser(newUser);
    }

    /**
     * Создание пользователя.
     */
    public User create(User user) {
        Validator.validateUser(user);
        log.info("Создан новый пользователь - {}.", user);
        return userRepository.createUser(user);
    }
}
