package ru.yandex.practicum.filmorate.dal.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NoUsersFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class UserRepository extends BaseRepository<User> implements UserStorage {

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    private static final String DELETE_ALL_USERS_QUERY = "DELETE FROM users";
    private static final String DELETE_BY_ID_USER_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String FIND_BY_ID_USER_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends (user_id,friend_id,status) VALUES (?,?,?)";
    private static final String GET_ID_USER_FRIENDS_QUERY = "SELECT friend_id FROM friends WHERE user_id = ?";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String CREATE_USER_QUERY = "INSERT INTO users(email,login,name,birthday)" +
            "VALUES (?,?,?,?)";
    private static final String UPDATE_USER_QUERY = """
            UPDATE users
            SET email = ?, login = ?, name = ?, birthday = ?
            WHERE user_id = ?
            """;
    private static final String UPDATE_STATUS_QUERY = """
            UPDATE friends
            SET status = ?
            WHERE user_id = ? AND friend_id = ?
            """;
    private static final String FIND_ALL_FRIENDS_USER_QUERY = """
            SELECT u.*
            FROM users u
            JOIN friends f ON u.user_id = f.friend_id
            WHERE f.user_id = ?
            """;

    /**
     * Удаление из друзей.
     */
    @Override
    public void removeFriend(long userId, long friendId) {
        log.info("Удаление записи о дружбе из базе данных , UserID: {} , удалил из друзей FriendID: {}.", userId, friendId);
        getUserById(userId);
        getUserById(friendId);
        delete(DELETE_FRIEND_QUERY, userId, friendId);
        log.info("Удалена запись о дружбе из базы данных , UserID: {} , удалил из друзей FriendID: {}.", userId, friendId);
    }

    /**
     * Обновление статуса.
     */
    public void updateStatus(long userId, long friendId, String status) {
        log.info("Обновление записи статуса дружбы у пользователя в базе данных , UserID: {}.", userId);
        getUserById(userId);
        getUserById(friendId);
        update(UPDATE_STATUS_QUERY, status, userId, friendId);
        log.info("Обновлена запись статуса дружбы у пользователя в базе данных , UserID: {} , статус {}", userId, status);
    }

    /**
     * Добавление в друзья.
     */
    @Override
    public void addFriend(long userId, long friendId, String status) {
        log.info("Добавление записи о дружбе в базу данных , UserID: {} , добавляет FriendID: {}.", userId, friendId);
        getUserById(userId);
        getUserById(friendId);
        add(ADD_FRIEND_QUERY, userId, friendId, status);
        log.info("Добавлена запись о дружбе в базу данных , UserID: {} , добавил FriendID: {}.", userId, friendId);
    }

    /**
     * Получение всех друзей пользователя.
     */
    @Override
    public List<User> getAllFriends(long userId) {
        log.info("Получение= всех друзей пользователя из базы данных , UserID {}.", userId);
        List<User> users = findMany(FIND_ALL_FRIENDS_USER_QUERY, userId);
        for (User user : users) {
            user.setFriendsId(getIdUserFriends(user.getId()));
        }
        log.info("Получены все друзья пользователя из базы данных , UserID {}.", userId);
        return users;
    }

    /**
     * Удаление всех пользователей.
     */
    @Override
    public boolean removeAllUsers() {
        log.info("Удаление всех пользователей из базы данных.");
        boolean result = deleteAll(DELETE_ALL_USERS_QUERY);
        log.info("Удалены все пользователи из базы данных.");
        return result;
    }

    /**
     * Удаление пользователя по ID.
     */
    @Override
    public boolean removeFriendById(long userId) {
        log.info("Удаление пользователя из базы данных , UserID: {}.", userId);
        getUserById(userId);
        boolean result = delete(DELETE_BY_ID_USER_QUERY, userId);
        log.info("Удалён пользователь из базы данных , UserID: {}.", userId);
        return result;
    }

    /**
     * Получение пользователя по ID.
     */
    @Override
    public User getUserById(long userId) {
        log.info("Получение пользователя из базы данных, UserID: {}.", userId);
        User user = findOne(FIND_BY_ID_USER_QUERY, userId).orElseThrow(() -> {
            String errorMessage = "Пользователя с ID - " + userId + " не существует.";
            log.warn("Ошибка пользователя по ID {}: {}", userId, errorMessage);
            return new NotFoundException(errorMessage);
        });
        user.setFriendsId(getIdUserFriends(userId));
        log.info("Получен пользователь из базы данных, UserID: {}.", userId);
        return user;
    }

    /**
     * Получение ID друзей пользователя.
     */
    private Set<Long> getIdUserFriends(long userId) {
        log.info("Получение всех ID друзей , пользователя из базы данных, UserID: {}.", userId);
        Set<Long> idFriends = new HashSet<>(findManyFriendsId(GET_ID_USER_FRIENDS_QUERY, userId));
        log.info("Получены все ID друзей , пользователя из базы данных, UserID: {}.", userId);
        return idFriends;
    }

    /**
     * Получение всех пользователей.
     */
    @Override
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей из базы данных.");
        List<User> users = findMany(FIND_ALL_USERS_QUERY);
        if (users.isEmpty()) {
            throw new NoUsersFoundException("Пользователи не найдены: база данных пуста.");
        }
        for (User user : users) {
            user.setFriendsId(getIdUserFriends(user.getId()));
        }
        log.info("Получены все пользователи из базы данных.");
        return users;
    }

    /**
     * Обновление пользователя.
     */
    @Override
    public User updateUser(User newUser) {
        log.info("Обновление записи пользователя в базе данных: {}.", newUser);
        getUserById(newUser.getId());
        update(UPDATE_USER_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId());
        log.info("Обновлена запись о пользователе в базе данных: {}.", newUser);
        return newUser;
    }

    /**
     * Создание пользователя.
     */
    @Override
    public User createUser(User user) {
        log.info("Создание записи пользователя в базе данных: {}.", user);
        long id = insert(CREATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        log.info("Создана запись о пользователя в базе данных: {}.", user);
        return user;
    }
}
