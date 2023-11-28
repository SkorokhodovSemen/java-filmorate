package ru.yandex.practicum.filmorate.dbstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbstorage.dao.FilmLikesStorageDao;
import ru.yandex.practicum.filmorate.dbstorage.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilmLikesDbStorage implements FilmLikesStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorageDao filmStorageDao;

    public FilmLikesDbStorage(JdbcTemplate jdbcTemplate, FilmStorageDao filmStorageDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorageDao = filmStorageDao;
    }


    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "select film_id from film order by rate desc limit ?";
        List<Integer> idList = jdbcTemplate.queryForList(sql, Integer.class, count);
        return idList.stream().map(filmStorageDao::findById).sorted((f1, f2) -> {
            if (f1.getRate() > f2.getRate()) return -1;
            else return 1;
        }).collect(Collectors.toList());
    }

    @Override
    public Film deleteLike(int idFilm, int idUser) {
        String sql = "delete from film_likes " +
                "where (id_film = ? and id_user = ?)";
        jdbcTemplate.update(sql, idFilm, idUser);
        String sqlRate = "update film set rate = (rate -1) where film_id = ?";
        jdbcTemplate.update(sqlRate, idFilm);
        return filmStorageDao.findById(idFilm);
    }

    @Override
    public Film addLikes(int idFilm, int idUser) {
        String sql = "insert into film_likes (id_film, id_user) " +
                "values (?, ?)";
        jdbcTemplate.update(sql, idFilm, idUser);
        String sqlRate = "update film set rate = (rate + 1) where film_id = ?";
        jdbcTemplate.update(sqlRate, idFilm);
        return filmStorageDao.findById(idFilm);
    }
}
