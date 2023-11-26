package ru.yandex.practicum.filmorate.dbstorage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRelationshipStorageDao {
    User addFriend(int idUser1, int idUser2);

    User deleteFriend(int idUser1, int idUser2);

    List<User> getCommonFriends(int idUser1, int idUser2);

    List<User> getAllFriends(int idUser);
}
