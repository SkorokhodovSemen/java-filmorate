package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController extends EntityController<Film, InMemoryFilmStorage> {
    private static final int MAX_SYMBOLS = 200;
    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage entityStorage, FilmService filmService) {
        super(entityStorage);
        this.filmService = filmService;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLikes(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return filmService.addLikes(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

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
