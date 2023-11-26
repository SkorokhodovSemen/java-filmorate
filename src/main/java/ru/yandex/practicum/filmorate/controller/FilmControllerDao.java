package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceDao;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmControllerDao {
    private Logger log = LoggerFactory.getLogger(Film.class);
    private FilmServiceDao filmServiceDao;

    @Autowired
    public FilmControllerDao(FilmServiceDao filmServiceDao) {
        this.filmServiceDao = filmServiceDao;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmServiceDao.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") int id) {
        return filmServiceDao.findById(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmServiceDao.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmServiceDao.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLikes(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return filmServiceDao.addLikes(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return filmServiceDao.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmServiceDao.getPopularFilms(count);
    }

}
