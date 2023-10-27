package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Количество пользователей в данный момент: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
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
        user.setId(id);
        users.put(id, user);
        log.debug("Пользователь {} сохранен и получил уникальный id {}", user, user.getId());
        id++;
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) {
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
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь неверно ввел id: {}", user.getId());
            throw new ValidationException("Нет пользователя с таким id");
        }
        users.put(user.getId(), user);
        log.debug("Пользователь {} обновил свои данные под id {}", user, user.getId());
        return user;
    }
}
