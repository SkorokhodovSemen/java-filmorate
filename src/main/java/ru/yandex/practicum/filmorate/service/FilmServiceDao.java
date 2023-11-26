package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dbstorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.dbstorage.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.dbstorage.FilmLikesDbStorage;
import ru.yandex.practicum.filmorate.dbstorage.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmServiceDao {
    private static final int MAX_SYMBOLS = 200;
    private static final LocalDate RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmLikesDbStorage filmLikesDbStorage;
    private final FilmGenreDbStorage filmGenreDbStorage;
    private final JdbcTemplate jdbcTemplate;
    private Logger log = LoggerFactory.getLogger(FilmServiceDao.class);

    @Autowired
    public FilmServiceDao(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage, FilmLikesDbStorage filmLikesDbStorage, FilmGenreDbStorage filmGenreDbStorage, JdbcTemplate jdbcTemplate) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
        this.filmLikesDbStorage = filmLikesDbStorage;
        this.filmGenreDbStorage = filmGenreDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> findAll() {
        return filmDbStorage.findAll();
    }

    public Film findById(int id) {
        validFound(id);
        return filmDbStorage.findById(id);
    }

    public Film create(Film film) {
        validate(film);
        Film filmSaved = filmDbStorage.create(film);
        if (!filmSaved.getGenres().isEmpty()) {
            for (Genre genre : filmSaved.getGenres()) {
                validFoundForGenre(genre.getId());
                filmGenreDbStorage.addFilm(filmSaved.getId(), genre.getId());
            }
        }
        return filmSaved;
    }

    public Film update(Film film) {
        validFound(film.getId());
        validate(film);
        return filmDbStorage.update(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmLikesDbStorage.getPopularFilms(count);
    }

    public Film deleteLike(int idFilm, int idUser) {
        validFound(idFilm);
        validFoundForUser(idUser);
        return filmLikesDbStorage.deleteLike(idFilm, idUser);
    }

    public Film addLikes(int idFilm, int idUser) {
        validFound(idFilm);
        validFoundForUser(idUser);
        return filmLikesDbStorage.addLikes(idFilm, idUser);
    }


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

    private void validFound(int idFilm) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from film where film_id = ?", idFilm);
        if (userRows.next()) {
        } else {
            throw new NotFoundException("id " + idFilm + " не найден");
        }
    }

    private void validFoundForUser(int idUser) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from user_filmorate where user_id = ?", idUser);
        if (userRows.next()) {
        } else {
            throw new NotFoundException("id " + idUser + " не найден");
        }
    }

    private void validFoundForGenre(int idGenre) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from genre where id = ?", idGenre);
        if (userRows.next()) {
        } else {
            throw new NotFoundException("id " + idGenre + " не найден");
        }
    }
}
