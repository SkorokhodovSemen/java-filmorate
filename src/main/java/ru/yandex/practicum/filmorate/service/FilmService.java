package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLikes(int idFilm, int idUser) {
        validFound(idFilm, idUser);
        filmStorage.getStorage().get(idFilm).addLikes(idUser);
        return filmStorage.getStorage().get(idFilm);
    }

    public Film deleteLike(int idFilm, int idUser) {
        validFound(idFilm, idUser);
        filmStorage.getStorage().get(idFilm).deleteLikes(idUser);
        return filmStorage.getStorage().get(idFilm);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> {
                    if (f1.getRate() > f2.getRate()) return 1;
                    else return -1;
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validFound(int idFilm, int idUser) {
        if (!filmStorage.getStorage().containsKey(idFilm)) {
            throw new NotFoundException("Фильм " + idFilm + " не найден");
        }
        if (!userStorage.getStorage().containsKey(idUser)) {
            throw new NotFoundException("Пользователь " + idUser + " не найден");
        }
    }
}
