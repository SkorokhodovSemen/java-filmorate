package ru.yandex.practicum.filmorate.dbstorage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorageDao {
    List<Film> findAll();

    Film findById(int id);

    int create(Film entity);

    int update(Film entity);
}
