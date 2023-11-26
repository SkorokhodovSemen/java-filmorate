package ru.yandex.practicum.filmorate.dbstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbstorage.dao.FilmGenreStorageDao;

@Component
public class FilmGenreDbStorage implements FilmGenreStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(int idFilm, int idGenre) {
        SqlRowSet filmRows = jdbcTemplate
                .queryForRowSet("select * from film_genre where (id_film = ? and genre_id = ?)", idFilm, idGenre);
        if (!filmRows.next()) {
            String sqlForGenre = "insert into film_genre (id_film, genre_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlForGenre, idFilm, idGenre);
        }
    }

    @Override
    public void deleteFilm(int idFilm) {
        String sqlForGenre = "delete from film_genre where id_film = ?";
        jdbcTemplate.update(sqlForGenre, idFilm);
    }
}
