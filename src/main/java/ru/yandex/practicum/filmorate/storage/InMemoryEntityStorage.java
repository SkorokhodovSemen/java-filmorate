package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryEntityStorage<T extends Entity> implements EntityStorage<T> {
    protected Map<Integer, T> storage = new HashMap<>();
    protected Logger log = LoggerFactory.getLogger(Entity.class);
    protected int id = 1;

    @Override
    public List<T> findAll() {
        log.info("Выполнен запрос на получение списка");
        return new ArrayList<>(storage.values());
    }

    @Override
    public T findById(int id) {
        if (!storage.containsKey(id)) {
            throw new NotFoundException("Введен несуществующий id: " + id);
        }
        log.info("Выполнен запрос на получение пользователя {}", id);
        return storage.get(id);
    }

    @Override
    public T create(T entity) {
        validate(entity);
        entity.setId(id);
        storage.put(id, entity);
        id++;
        return entity;
    }

    @Override
    public T update(T entity) {
        if (!storage.containsKey(entity.getId())) {
            log.info("Не найден id: {}", entity.getId());
            throw new NotFoundException("Id не найден, проверьте id");
        }
        validate(entity);
        storage.put(entity.getId(), entity);
        return entity;
    }

    public void validate(T entity) {
    }

    public Map<Integer, T> getStorage() {
        return storage;
    }
}
