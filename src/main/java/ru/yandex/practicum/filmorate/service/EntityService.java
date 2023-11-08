package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.InMemoryEntityStorage;

import java.util.List;

public abstract class EntityService<T extends Entity, K extends InMemoryEntityStorage> {
    protected K entityStorage;
    protected Logger log = LoggerFactory.getLogger(Entity.class);

    @Autowired
    public EntityService(K entityStorage) {
        this.entityStorage = entityStorage;
    }

    public List<T> findAll() {
        return entityStorage.findAll();
    }

    public T findById(int id) {
        return (T) entityStorage.findById(id);
    }

    public T create(T entity) {
        validate(entity);
        return (T) entityStorage.create(entity);
    }

    public T update(T entity) {
        validate(entity);
        return (T) entityStorage.update(entity);
    }

    abstract void validate(T entity);
}
