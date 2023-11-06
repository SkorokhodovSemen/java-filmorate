package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int idUser1, int idUser2) {
        validFound(idUser1, idUser2);
        userStorage.getStorage().get(idUser1).addFriend(idUser2);
        userStorage.getStorage().get(idUser2).addFriend(idUser1);
        return userStorage.getStorage().get(idUser1);
    }

    public User deleteFriend(int idUser1, int idUser2) {
        validFound(idUser1, idUser2);
        userStorage.getStorage().get(idUser1).deleteFriend(idUser2);
        userStorage.getStorage().get(idUser2).deleteFriend(idUser1);
        return userStorage.getStorage().get(idUser1);
    }

    public List<User> getCommonFriends(int idUser1, int idUser2) {
        validFound(idUser1, idUser2);
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> commonId = userStorage.getStorage().get(idUser1).getFriends().stream()
                .filter(userStorage.getStorage().get(idUser2).getFriends()::contains).collect(Collectors.toSet());
        for (Integer id : commonId) {
            commonFriends.add(userStorage.getStorage().get(id));
        }
        return commonFriends;
    }

    public List<User> getAllFriends(int idUser) {
        if (!userStorage.getStorage().containsKey(idUser)) {
            throw new NotFoundException("Пользователь " + idUser + " не найден");
        }
        List<User> friends = new ArrayList<>();
        for (Integer id : userStorage.getStorage().get(idUser).getFriends()) {
            friends.add(userStorage.getStorage().get(id));
        }
        return friends;
    }

    private void validFound(int idUser1, int idUser2) {
        if (!userStorage.getStorage().containsKey(idUser1)) {
            throw new NotFoundException("Пользователь " + idUser1 + " не найден");
        }
        if (!userStorage.getStorage().containsKey(idUser2)) {
            throw new NotFoundException("Пользователь " + idUser2 + " не найден");
        }
    }
}
