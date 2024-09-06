package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    /**
     * POST - создание пользователя.
     */
    User createUser(User user);

    /**
     * PUT - обновление пользователя.
     */
    User updateUser(User user);

    /**
     * PUT - добавление в друзья.
     */
    void addFriend(long userId, long friendId);

    /**
     * GET - получение пользователя во ID.
     */
    User getUserById(long userId);

    /**
     * GET - получение всех пользователей.
     */
    List<User> getAllUsers();

    /**
     * GET - возвращаем список пользователей, являющихся его друзьями.
     */
    List<User> getUserFriends(long userId);

    /**
     * GET - список друзей, общих с другим пользователем.
     */
    List<User> getCommonFriends(long userId, long otherId);

    /**
     * DELETE - удаление из друзей.
     */
    void removeFriend(long userId, long friendId);

    /**
     * DELETE - удаление пользоваетеля по ID.
     */
    void deleteUserById(long userId);

    /**
     * DELETE - удаление всех пользоваетелей.
     */
    void deleteAllUsers();
}
