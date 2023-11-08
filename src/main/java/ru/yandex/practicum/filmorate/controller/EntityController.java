package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.service.EntityService;

import java.util.List;

@RestController
public abstract class EntityController<T extends Entity, K extends EntityService> {
    protected Logger log = LoggerFactory.getLogger(Entity.class);
    protected K entityService;

    @Autowired
    public EntityController(K entityService) {
        this.entityService = entityService;
    }

    @GetMapping
    public List<T> findAll() {
        return entityService.findAll();
    }

    @GetMapping("/{id}")
    public T findById(@PathVariable("id") int id) {
        return (T) entityService.findById(id);
    }

    @PostMapping
    public T create(@RequestBody T entity) {
        return (T) entityService.create(entity);
    }

    @PutMapping
    public T update(@RequestBody T entity) {
        return (T) entityService.update(entity);
    }
}
