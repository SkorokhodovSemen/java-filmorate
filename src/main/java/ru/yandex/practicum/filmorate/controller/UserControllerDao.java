package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceDao;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserControllerDao {
    private Logger log = LoggerFactory.getLogger(Film.class);
    private UserServiceDao userServiceDao;

    @Autowired
    public UserControllerDao(UserServiceDao userServiceDao) {
        this.userServiceDao = userServiceDao;
    }

    @GetMapping
    public List<User> findAll() {
        return userServiceDao.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") int id) {
        return userServiceDao.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userServiceDao.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userServiceDao.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        return userServiceDao.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        return userServiceDao.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") int id) {
        return userServiceDao.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        return userServiceDao.getCommonFriends(id, otherId);
    }

}
