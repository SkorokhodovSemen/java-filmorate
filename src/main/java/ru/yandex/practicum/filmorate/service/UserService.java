package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dbstorage.UserDbStorage;
import ru.yandex.practicum.filmorate.dbstorage.UserRelationshipDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    protected Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;
    private final UserRelationshipDbStorage userRelationshipDbStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage, JdbcTemplate jdbcTemplate, UserRelationshipDbStorage userRelationshipDbStorage) {
        this.userDbStorage = userDbStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.userRelationshipDbStorage = userRelationshipDbStorage;
    }

    public List<User> findAll() {
        return userDbStorage.findAll();
    }

    public User findById(int id) {
        validFound(id);
        return userDbStorage.findById(id);
    }

    public User create(User user) {
        validate(user);
        return userDbStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        validFound(user.getId());
        return userDbStorage.update(user);
    }

    public void addFriend(int idUser1, int idUser2) {
        validFound(idUser1);
        validFound(idUser2);
        userRelationshipDbStorage.addFriend(idUser1, idUser2);
    }

    public void deleteFriend(int idUser1, int idUser2) {
        validFound(idUser1);
        validFound(idUser2);
        userRelationshipDbStorage.deleteFriend(idUser1, idUser2);
    }

    public List<User> getCommonFriends(int idUser1, int idUser2) {
        validFound(idUser1);
        validFound(idUser2);
        return userRelationshipDbStorage.getCommonFriends(idUser1, idUser2);
    }

    public List<User> getAllFriends(int idUser) {
        validFound(idUser);
        return userRelationshipDbStorage.getAllFriends(idUser);
    }

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

    private void validFound(int idUser) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from user_filmorate where user_id = ?", idUser);
        if (!userRows.next()) {
            throw new NotFoundException("id " + idUser + " не найден");
        }
    }
}
