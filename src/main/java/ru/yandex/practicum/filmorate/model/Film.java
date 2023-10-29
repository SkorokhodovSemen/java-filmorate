package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Data
public class Film extends Entity {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;

}
