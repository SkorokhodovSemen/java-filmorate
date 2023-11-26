package ru.yandex.practicum.filmorate.dbstorage.dao;

public interface FilmGenreStorageDao {
    void addFilm(int idFilm, int idGenre);

    void deleteFilm(int idFilm);
}
