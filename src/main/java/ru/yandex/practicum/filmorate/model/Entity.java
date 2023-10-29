package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Entity {
    protected int id;
}
