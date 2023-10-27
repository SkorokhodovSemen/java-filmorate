package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FilmController {
    private static final int MAX_SYMBOLS = 200;
    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Общее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) {
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
            log.info("Пользователь неверно ввел id фильма: {}", film.getId());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        film.setId(id);
        films.put(id, film);
        id++;
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
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
            log.info("Пользователь неверно указал продолжительность фильма: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        if (!films.containsKey(film.getId())) {
            log.info("Пользователь неверно ввел id фильма: {}", film.getId());
            throw new ValidationException("Нет фильма с таким id");
        }
        films.put(film.getId(), film);
        log.debug("Пользователь обновил фильм {} под id {}", film, film.getId());
        return film;
    }
}
