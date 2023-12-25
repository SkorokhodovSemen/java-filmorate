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
    public List<Film> getLikedFilms(int idUser) {
        String sql = "select * from film as f" +
                " inner join film_likes as f_l on f.film_id = f_l.id_film" +
                " inner join mpa as m on f.mpa = m.id" +
                " where f_l.id_user = ?" +
                " order by f.rate desc";
        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, idUser);
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
