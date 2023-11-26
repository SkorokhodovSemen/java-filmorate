package ru.yandex.practicum.filmorate.dbstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbstorage.dao.FilmGenreStorageDao;
import ru.yandex.practicum.filmorate.dbstorage.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
public class FilmDbStorage implements FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorageDao filmGenreStorageDao;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmGenreStorageDao filmGenreStorageDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreStorageDao = filmGenreStorageDao;
    }


    @Override
    public List findAll() {
        String sql = "select * from film";
        return jdbcTemplate.query(sql, this::makeFilm);
    }

    @Override
    public Film findById(int id) {
        validFound(id);
        String sql = "select * from film where film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeFilm, id);
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into film (name, description, release_data, duration, mpa, rate) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlQuery, new String[]{"film_id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, (int) film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            statement.setInt(6, film.getRate());
            return statement;
        }, keyHolder);
        if (!film.getGenres().isEmpty()) {
            String sqlForClearGenre = "delete from film_genre where id_film = ?";
            jdbcTemplate.update(sqlForClearGenre, film.getId());
            for (Genre genre : film.getGenres()) {
                filmGenreStorageDao.addFilm(keyHolder.getKey().intValue(), genre.getId());
            }
        }
        String sql = "select * from film where film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeFilm, keyHolder.getKey().intValue());
    }

    @Override
    public Film update(Film film) {
        validFound(film.getId());
        String sqlQuery = "update film set " +
                "name = ?, description = ?, release_data = ?, duration = ?, mpa = ? , rate = ?" +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate(),
                film.getId());
        if (!film.getGenres().isEmpty()) {
            String sqlForClearGenre = "delete from film_genre where id_film = ?";
            jdbcTemplate.update(sqlForClearGenre, film.getId());
            for (Genre genre : film.getGenres()) {
                filmGenreStorageDao.addFilm(film.getId(), genre.getId());
            }
        } else {
            filmGenreStorageDao.deleteFilm(film.getId());
        }
        String sql = "select * from film where film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeFilm, film.getId());
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa"));
        String sqlForMpa = "select rate from mpa where id = ?";
        mpa.setName(jdbcTemplate.queryForObject(sqlForMpa, String.class, mpa.getId()));
        Set<Genre> genres = new TreeSet<>((genre1, genre2) -> {
            if (genre1.getId() < genre2.getId()) return -1;
            else return 1;
        });
        SqlRowSet filmRows = jdbcTemplate
                .queryForRowSet("select * from film_genre where id_film = ?", rs.getInt("film_id"));
        if (filmRows.next()) {
            String sql = "select genre_id from film_genre where id_film = ?";
            List<Integer> idList = jdbcTemplate.queryForList(sql, Integer.class, rs.getInt("film_id"));
            for (Integer id : idList) {
                String sqlForFilmGenre = "select genre from genre where id = ?";
                String genreName = jdbcTemplate.queryForObject(sqlForFilmGenre, String.class, id);
                Genre savedGenre = new Genre();
                savedGenre.setId(id);
                savedGenre.setName(genreName);
                genres.add(savedGenre);
            }
        }
        film.setId(rs.getInt("film_id"));
        film.setMpa(mpa);
        film.setDuration(rs.getInt("duration"));
        film.setDescription(rs.getString("description"));
        film.setName(rs.getString("name"));
        film.setReleaseDate(rs.getDate("release_data").toLocalDate());
        film.setRate(rs.getInt("rate"));
        film.setGenres(genres);
        return film;
    }

    private void validFound(int idFilm) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where film_id = ?", idFilm);
        if (!filmRows.next()) {
            throw new NotFoundException("id " + idFilm + " не найден");
        }
    }
}
