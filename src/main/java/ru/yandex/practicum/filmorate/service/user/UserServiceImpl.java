package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoUsersFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.service.event.EventServiceImpl;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage jdbcUserRepository;
    private final EventServiceImpl eventServiceImpl;
    private static final String CONFIRMED = "CONFIRMED";
    private static final String UNCONFIRMED = "UNCONFIRMED";


    @Autowired
    public UserServiceImpl(UserStorage jdbcUserRepository, EventServiceImpl eventServiceImpl) {
        this.jdbcUserRepository = jdbcUserRepository;
        this.eventServiceImpl = eventServiceImpl;
    }

    /**
     * Создание пользователя.
     */
    public User createUser(User user) {
        Validator.validateUser(user);
        User userResult = jdbcUserRepository.createUser(user);
        log.info("Создан пользователь:\n{}", userResult);
        return userResult;
    }

    /**
     * Обновление пользователя.
     */
    public User updateUser(User user) {
        User oldUser = jdbcUserRepository.getUserById(user.getId());
        log.info("Старый пользователь:\n{}", oldUser);
        Validator.validateUser(user);
        User userResult = jdbcUserRepository.updateUser(user);
        log.info("Обновлённый пользователь:\n{}", userResult);
        return userResult;
    }

    /**
     * Добавление в друзья.
     */
    public void addFriend(long userId, long friendId) {
        getUserById(userId);
        User userFriend = getUserById(friendId);
        log.info("Пользователь ID: {} добавляет в друзья пользователя ID: {}.", userId, friendId);
        String status = UNCONFIRMED;
        if (userFriend.getFriendsId().contains(userId)) {
            status = CONFIRMED;
            jdbcUserRepository.updateStatus(friendId, userId, status);
        }
        jdbcUserRepository.addFriend(userId, friendId,status);
        eventServiceImpl.createEvent(userId, EventType.FRIEND, Operation.ADD,friendId);
        log.info("Пользователь ID: {} добавил в друзья пользователя ID: {}.", userId, friendId);
    }

    /**
     * Удаление из друзей.
     */
    public void removeFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User userFriend = getUserById(friendId);
        log.info("Пользователь ID: {} удаляет из друзей пользователя ID: {}.", userId, friendId);
        user.getFriendsId().remove(friendId);
        if (userFriend.getFriendsId().contains(userId)) {
            jdbcUserRepository.updateStatus(friendId, userId, UNCONFIRMED);
        }
        jdbcUserRepository.removeFriend(userId, friendId);
        eventServiceImpl.createEvent(userId, EventType.FRIEND, Operation.REMOVE,friendId);
        log.info("Пользователь c ID: {} удалил из друзей пользователя с ID: {}.", userId, friendId);
    }


    /**
     * Получение пользователя по ID.
     */
    public User getUserById(long userId) {
        log.info("Получен пользователь. ID пользователя: {}.", userId);
        return jdbcUserRepository.getUserById(userId);
    }

    /**
     * Получение всех пользователей.
     */
    public List<User> getAllUsers() {
        log.info("Получение списка всех пользователей.");
        List<User> users = jdbcUserRepository.getAllUsers();
        if (users.isEmpty()) {
            log.error("Список пользователей пуст.");
            throw new NoUsersFoundException("Список пользователей пуст.");
        }
        log.info("Все пользователи получены.");
        return users;
    }

    /**
     * Возвращаем список пользователей, являющихся его друзьями.
     */
    public List<User> getUserFriends(long userId) {
        jdbcUserRepository.getUserById(userId);
        log.info("Получение списка всех друзей пользователя. ID пользователя {}.", userId);
        return jdbcUserRepository.getAllFriends(userId);
    }

    /**
     * Список друзей, общих с другим пользователем.
     */
    public List<User> getCommonFriends(long userId, long otherId) {
        User user = getUserById(userId);
        User userOther = getUserById(otherId);
        log.info("Получение списка общих друзей пользователя {} с пользователем {}.", user.getName(), userOther.getName());
        List<User> commonFriends = new ArrayList<>();
        for (Long id : user.getFriendsId()) {
            if (userOther.getFriendsId().contains(id)) {
                commonFriends.add(getUserById(id));
            }
        }
        log.info("Получен список общих друзей пользователя {} с пользователем {}.", user.getName(), userOther.getName());
        return commonFriends;
    }


    /**
     * Удаление пользователя по ID.
     */
    public void deleteUserById(long userId) {
        getUserById(userId);
        jdbcUserRepository.deleteUserById(userId);
        log.info("Пользователь с ID - {} удалён.", userId);
    }

    /**
     * Удаление всех пользователей.
     */
    public void deleteAllUsers() {
        jdbcUserRepository.deleteAllUsers();
        log.info("Удаление всех пользователей.");
    }
}
