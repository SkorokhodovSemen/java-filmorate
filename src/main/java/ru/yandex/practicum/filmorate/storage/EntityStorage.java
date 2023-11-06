package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.List;

public interface EntityStorage<T extends Entity> {
    List<T> findAll();

    T findById(int id);

    T create(T entity);

    T update(T entity);
}
