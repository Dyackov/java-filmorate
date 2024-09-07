package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class JdbcUserRepository extends BaseRepository<User> implements UserStorage {

    public JdbcUserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    private static final String CREATE_USER_QUERY = """
            INSERT INTO users(email,login,name,birthday)
            VALUES (?,?,?,?)
            """;
    private static final String UPDATE_USER_QUERY = """
            UPDATE users
            SET email = ?, login = ?, name = ?, birthday = ?
            WHERE user_id = ?
            """;
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends (user_id,friend_id,status) VALUES (?,?,?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";



    private static final String DELETE_ALL_USERS_QUERY = "DELETE FROM users";
    private static final String DELETE_BY_ID_USER_QUERY = "DELETE FROM users WHERE user_id = ?";

    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";



    private static final String GET_ID_USER_FRIENDS_QUERY = "SELECT friend_id FROM friends WHERE user_id = ?";

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
     * Создание пользователя.
     */
    @Override
    public User createUser(User user) {
        long id = insert(CREATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        log.debug("Пользователь сохранён в базе данных.\n{}.", user);
        return user;
    }

    /**
     * Обновление пользователя.
     */
    @Override
    public User updateUser(User user) {
        update(UPDATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.debug("Пользователь обновлён базе данных.\n{}.", user);
        return user;
    }

    /**
     * Добавление в друзья.
     */
    @Override
    public void addFriend(long userId, long friendId,String status) {
        log.info("Добавление записи о дружбе в базу данных , UserID: {} , добавляет FriendID: {}.", userId, friendId);
        add(ADD_FRIEND_QUERY, userId, friendId,status);
        log.info("Добавлена запись о дружбе в базу данных , UserID: {} , добавил FriendID: {}.", userId, friendId);
    }


    /**
     * Получение пользователя по ID.
     */
    @Override
    public User getUserById(long userId) {
        User user = findOne(GET_USER_BY_ID_QUERY, userId).orElseThrow(() -> {
            String errorMessage = "Пользователя с ID - " + userId + " не существует.";
            log.warn("Ошибка пользователя по ID {}: {}", userId, errorMessage);
            return new NotFoundException(errorMessage);
        });
        user.setFriendsId(getIdUserFriends(userId));
        log.debug("Получен пользователь из базы данных. ID пользователя: {}.", userId);
        return user;
    }
    /**
     * Удаление из друзей.
     */
    @Override
    public void removeFriend(long userId, long friendId) {
        log.info("Удаление записи о дружбе из базе данных , UserID: {} , удалил из друзей FriendID: {}.", userId, friendId);
        delete(DELETE_FRIEND_QUERY, userId, friendId);
        log.info("Удалена запись о дружбе из базы данных , UserID: {} , удалил из друзей FriendID: {}.", userId, friendId);
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
    public void deleteAllUsers() {
        log.info("Удаление всех пользователей из базы данных.");
        deleteAll(DELETE_ALL_USERS_QUERY);
        log.info("Удалены все пользователи из базы данных.");

    }

    /**
     * Удаление пользователя по ID.
     */
    @Override
    public void deleteUserById(long userId) {
        log.info("Удаление пользователя из базы данных , UserID: {}.", userId);
        delete(DELETE_BY_ID_USER_QUERY, userId);
        log.info("Удалён пользователь из базы данных , UserID: {}.", userId);
    }

    /**
     * Получение всех пользователей.
     */
    @Override
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей из базы данных.");
        List<User> users = findMany(FIND_ALL_USERS_QUERY);
        for (User user : users) {
            user.setFriendsId(getIdUserFriends(user.getId()));
        }
        log.info("Получены все пользователи из базы данных.");
        return users;
    }

    /**
     * Обновление статуса.
     */
    @Override
    public void updateStatus(long userId, long friendId, String status) {
        log.info("Обновление записи статуса дружбы у пользователя в базе данных , UserID: {}.", userId);
        update(UPDATE_STATUS_QUERY, status, userId, friendId);
        log.info("Обновлена запись статуса дружбы у пользователя в базе данных , UserID: {} , статус {}", userId, status);
    }

    /**
     * Получение ID друзей пользователя.
     */
    private Set<Long> getIdUserFriends(long userId) {
        log.debug("Получение всех ID друзей , пользователя из базы данных, UserID: {}.", userId);
        Set<Long> idFriends = new HashSet<>(findManyFriendsId(GET_ID_USER_FRIENDS_QUERY, userId));
        log.debug("Получены все ID друзей , пользователя из базы данных, UserID: {}.", userId);
        return idFriends;
    }

}
