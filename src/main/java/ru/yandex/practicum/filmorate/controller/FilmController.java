package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController extends EntityController<Film> {
    private static final int MAX_SYMBOLS = 200;
    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);

    @Override
    void validate(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.info("Пользователь неверно ввел имя фильма: {}", film.getName());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().isBlank() || film.getDescription().length() > MAX_SYMBOLS) {
            log.info("Пользователь неверно ввел описание фильма {}, количество символов {}", film.getDescription(), film.getDescription().length());
            throw new ValidationException("Описание не может быть пустым или превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATA)) {
            log.info("Пользователь неверно указал дату релиза: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.info("Пользователь неверно ввел продолжительность фильма: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
