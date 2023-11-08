package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends EntityService<User, InMemoryUserStorage> {
    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        super(userStorage);
    }

    @Override
    void validate(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@") || user.getEmail() == null) {
            log.info("Пользователь неверно ввел почту: {}", user.getEmail());
            throw new ValidationException("Неверный формат почты или поле не заполнено");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ") || user.getLogin() == null) {
            log.info("Пользователь неверно ввел логин: {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Пользователь неверно ввел дату рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User addFriend(int idUser1, int idUser2) {
        validFound(idUser1, idUser2);
        entityStorage.getStorage().get(idUser1).addFriend(idUser2);
        entityStorage.getStorage().get(idUser2).addFriend(idUser1);
        return entityStorage.getStorage().get(idUser1);
    }

    public User deleteFriend(int idUser1, int idUser2) {
        validFound(idUser1, idUser2);
        entityStorage.getStorage().get(idUser1).deleteFriend(idUser2);
        entityStorage.getStorage().get(idUser2).deleteFriend(idUser1);
        return entityStorage.getStorage().get(idUser1);
    }

    public List<User> getCommonFriends(int idUser1, int idUser2) {
        validFound(idUser1, idUser2);
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> commonId = entityStorage.getStorage().get(idUser1).getFriends().stream()
                .filter(entityStorage.getStorage().get(idUser2).getFriends()::contains).collect(Collectors.toSet());
        for (Integer id : commonId) {
            commonFriends.add(entityStorage.getStorage().get(id));
        }
        return commonFriends;
    }

    public List<User> getAllFriends(int idUser) {
        if (!entityStorage.getStorage().containsKey(idUser)) {
            throw new NotFoundException("Пользователь " + idUser + " не найден");
        }
        List<User> friends = new ArrayList<>();
        for (Integer id : entityStorage.getStorage().get(idUser).getFriends()) {
            friends.add(entityStorage.getStorage().get(id));
        }
        return friends;
    }

    private void validFound(int idUser1, int idUser2) {
        if (!entityStorage.getStorage().containsKey(idUser1)) {
            throw new NotFoundException("Пользователь " + idUser1 + " не найден");
        }
        if (!entityStorage.getStorage().containsKey(idUser2)) {
            throw new NotFoundException("Пользователь " + idUser2 + " не найден");
        }
    }
}
