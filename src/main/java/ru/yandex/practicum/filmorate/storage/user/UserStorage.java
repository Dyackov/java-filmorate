package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {


    void updateStatus(long userId, long friendId, String status);

    void removeFriend(long userId, long friendId);

    List<User> getAllFriends(long userId);

    void addFriend(long userId, long friendId, String status);

    boolean removeAllUsers();

    boolean removeFriendById(long userId);

    User getUserById(long userId);

    List<User> getAllUsers();

    User updateUser(User newUser);

    User createUser(User user);
}
