package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public abstract class EntityController<T extends Entity> {
    protected Map<Integer, T> storage = new HashMap<>();
    protected Logger log = LoggerFactory.getLogger(Entity.class);
    protected int id = 1;

    @GetMapping
    public List<T> findAll() {
        log.info("Выполнен запрос на получение списка");
        return new ArrayList<>(storage.values());
    }

    @PostMapping
    public T create(@RequestBody T entity) {
        validate(entity);
        entity.setId(id);
        storage.put(id, entity);
        id++;
        return entity;
    }

    @PutMapping
    public T update(@RequestBody T entity) {
        if (!storage.containsKey(entity.getId())) {
            log.info("Не найден id: {}", entity.getId());
            throw new ValidationException("Id не найден, проверьте id");
        }
        validate(entity);
        storage.put(entity.getId(), entity);
        return entity;
    }

    abstract void validate(T entity);
}
