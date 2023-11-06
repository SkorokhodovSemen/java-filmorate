package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends EntityController<User, InMemoryUserStorage> {
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage entityStorage, UserService userService) {
        super(entityStorage);
        this.userService = userService;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") int id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        return userService.getCommonFriends(id, otherId);
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
}
