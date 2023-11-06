package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public class InMemoryFilmStorage extends InMemoryEntityStorage<Film> {
    private static final int MAX_SYMBOLS = 200;
    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);

    @Override
    public void validate(Film film) {
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
