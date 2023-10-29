package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Data
public class User extends Entity {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
