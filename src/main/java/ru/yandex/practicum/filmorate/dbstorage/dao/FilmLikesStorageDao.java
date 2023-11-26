package ru.yandex.practicum.filmorate.dbstorage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmLikesStorageDao {
    List<Film> getPopularFilms(int count);

    Film deleteLike(int idFilm, int idUser);

    Film addLikes(int idFilm, int idUser);

}
