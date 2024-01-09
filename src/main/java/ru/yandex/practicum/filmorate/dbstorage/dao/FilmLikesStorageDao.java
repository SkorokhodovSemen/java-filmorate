package ru.yandex.practicum.filmorate.dbstorage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmLikesStorageDao {
    List<Film> getPopularFilms(int count);

    List<Film> getRecommendations(int idUser);

    void deleteLike(int idFilm, int idUser);

    void addLikes(int idFilm, int idUser);

}
