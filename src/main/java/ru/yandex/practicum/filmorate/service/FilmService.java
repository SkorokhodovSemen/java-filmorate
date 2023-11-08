package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends EntityService<Film, InMemoryFilmStorage> {
    private static final int MAX_SYMBOLS = 200;
    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        super(filmStorage);
        this.userStorage = userStorage;
    }

    public Film addLikes(int idFilm, int idUser) {
        validFound(idFilm, idUser);
        entityStorage.getStorage().get(idFilm).addLikes(idUser);
        return entityStorage.getStorage().get(idFilm);
    }

    public Film deleteLike(int idFilm, int idUser) {
        validFound(idFilm, idUser);
        entityStorage.getStorage().get(idFilm).deleteLikes(idUser);
        return entityStorage.getStorage().get(idFilm);
    }

    public List<Film> getPopularFilms(int count) {
        return entityStorage.findAll().stream()
                .sorted((f1, f2) -> {
                    if (f1.getRate() > f2.getRate()) return 1;
                    else return -1;
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validFound(int idFilm, int idUser) {
        if (!entityStorage.getStorage().containsKey(idFilm)) {
            throw new NotFoundException("Фильм " + idFilm + " не найден");
        }
        if (!userStorage.getStorage().containsKey(idUser)) {
            throw new NotFoundException("Пользователь " + idUser + " не найден");
        }
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
