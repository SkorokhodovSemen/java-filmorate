package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServiceDao;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreControllerDao {

    private GenreServiceDao genreServiceDao;

    @Autowired
    public GenreControllerDao(GenreServiceDao genreServiceDao) {
        this.genreServiceDao = genreServiceDao;
    }

    @GetMapping
    public List<Genre> findAll() {
        return genreServiceDao.findAll();
    }

    @GetMapping("/{id}")
    public Genre findById(@PathVariable("id") int id) {
        return genreServiceDao.findById(id);
    }
}
