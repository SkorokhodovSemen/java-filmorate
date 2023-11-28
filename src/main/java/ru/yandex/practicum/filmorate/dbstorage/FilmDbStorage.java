package ru.yandex.practicum.filmorate.dbstorage;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbstorage.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List findAll() {
        String sql = "select * from film as f inner join mpa as m on f.mpa = m.id";
        return jdbcTemplate.query(sql, this::makeFilm);
    }

    @Override
    public Film findById(int id) {
        String sql = "select * from film as f inner join mpa as m on f.mpa = m.id where f.film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeFilm, id);
    }

    @Override
    public int create(Film film) {
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
            List<Genre> genres = new ArrayList<>(film.getGenres());
            jdbcTemplate.batchUpdate("insert into film_genre (id_film, genre_id) values (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, keyHolder.getKey().intValue());
                            ps.setInt(2, genres.get(i).getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return genres.size();
                        }
                    });
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public int update(Film film) {
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
            String sqlDelete = "delete from film_genre where id_film = ?";
            jdbcTemplate.update(sqlDelete,film.getId());
            List<Genre> genres = new ArrayList<>(film.getGenres());
            jdbcTemplate.batchUpdate("insert into film_genre (id_film, genre_id) values (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, film.getId());
                            ps.setInt(2, genres.get(i).getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return genres.size();
                        }
                    });
        } else {
            String sqlForGenre = "delete from film_genre where id_film = ?";
            jdbcTemplate.update(sqlForGenre, film.getId());
        }
        return film.getId();
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa"));
        mpa.setName(rs.getString("name_rate"));
        Set<Genre> genres = new TreeSet<>((genre1, genre2) -> {
            if (genre1.getId() < genre2.getId()) return -1;
            else return 1;
        });
        String sql = "select fg.genre_id, g.genre from film_genre as fg " +
                "inner join genre as g on g.id = fg.genre_id " +
                "where fg.id_film = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, rs.getInt("film_id"));
        if (result != null) {
            for (Map<String, Object> map : result) {
                Genre savedGenre = new Genre();
                savedGenre.setId((Integer) map.get("genre_id"));
                savedGenre.setName((String) map.get("genre"));
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
}
