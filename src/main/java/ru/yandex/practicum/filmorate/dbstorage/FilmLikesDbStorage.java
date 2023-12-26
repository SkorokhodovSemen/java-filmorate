package ru.yandex.practicum.filmorate.dbstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbstorage.dao.FilmLikesStorageDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public class FilmLikesDbStorage implements FilmLikesStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmLikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "select * from film as f inner join mpa as m on f.mpa = m.id order by rate desc limit ?";
        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, count);
    }

    @Override
    public List<Film> getPopularFilmsWithGenre(int count, int idGenre) {
        String sql = "select * from film as f inner join mpa as m on f.mpa = m.id " +
                "where (film_id = (select id_film from film_genre where genre_id = ?)) order by rate desc limit ?";
        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, idGenre, count);
    }

    @Override
    public List<Film> getPopularFilmsWithYear(int count, int year) {
        String sql = "select * from film as f inner join mpa as m on f.mpa = m.id " +
                "where (select extract (year from cast (release_data as date)) = ?) " +
                "order by rate desc limit ?";
        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, year, count);
    }

    @Override
    public List<Film> getPopularFilmsWithGenreAndYear(int count, int idGenre, int year) {
        String sql = "select * from film as f inner join mpa as m on f.mpa = m.id " +
                "where (select extract (year from cast (release_data as date)) = ?" +
                "and film_id = (select id_film from film_genre where genre_id = ?)) order by rate desc limit ?";
        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, year, idGenre, count);
    }

    @Override
    public void deleteLike(int idFilm, int idUser) {
        String sql = "delete from film_likes " +
                "where (id_film = ? and id_user = ?)";
        jdbcTemplate.update(sql, idFilm, idUser);
        String sqlRate = "update film set rate = (rate -1) where film_id = ?";
        jdbcTemplate.update(sqlRate, idFilm);
    }

    @Override
    public void addLikes(int idFilm, int idUser) {
        String sql = "insert into film_likes (id_film, id_user) " +
                "values (?, ?)";
        jdbcTemplate.update(sql, idFilm, idUser);
        String sqlRate = "update film set rate = (rate + 1) where film_id = ?";
        jdbcTemplate.update(sqlRate, idFilm);
    }
}
