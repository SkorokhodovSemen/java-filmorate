package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaServiceDao;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaControllerDao {
    private MpaServiceDao mpaServiceDao;

    @Autowired
    public MpaControllerDao(MpaServiceDao mpaServiceDao) {
        this.mpaServiceDao = mpaServiceDao;
    }

    @GetMapping
    public List<Mpa> findAll() {
        return mpaServiceDao.findAll();
    }

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable("id") int id) {
        return mpaServiceDao.findById(id);
    }
}
