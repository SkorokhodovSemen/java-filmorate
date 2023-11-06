package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.InMemoryEntityStorage;

import java.util.List;

@RestController
public abstract class EntityController<T extends Entity, K extends InMemoryEntityStorage> {
    protected Logger log = LoggerFactory.getLogger(Entity.class);
    protected K entityStorage;

    @Autowired
    public EntityController(K entityStorage) {
        this.entityStorage = entityStorage;
    }

    @GetMapping
    public List<T> findAll() {
        return entityStorage.findAll();
    }

    @GetMapping("/{id}")
    public T findById(@PathVariable("id") int id) {
        return (T) entityStorage.findById(id);
    }

    @PostMapping
    public T create(@RequestBody T entity) {
        return (T) entityStorage.create(entity);
    }

    @PutMapping
    public T update(@RequestBody T entity) {
        return (T) entityStorage.update(entity);
    }

    abstract void validate(T entity);
}
