package ru.yandex.practicum.filmorate.dbstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbstorage.dao.UserRelationshipStorageDao;
import ru.yandex.practicum.filmorate.dbstorage.dao.UserStorageDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRelationshipDbStorage implements UserRelationshipStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorageDao userStorageDao;

    public UserRelationshipDbStorage(JdbcTemplate jdbcTemplate, UserStorageDao userStorageDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorageDao = userStorageDao;
    }

    @Override
    public User addFriend(int idUser1, int idUser2) {
        String sql = "insert into relationship (id, id_friend) values (?, ?)";
        jdbcTemplate.update(sql, idUser1, idUser2);
        return userStorageDao.findById(idUser1);
    }

    @Override
    public User deleteFriend(int idUser1, int idUser2) {
        String sql = "delete from relationship where (id = ? and id_friend = ?)";
        jdbcTemplate.update(sql, idUser1, idUser2);
        return userStorageDao.findById(idUser1);
    }

    @Override
    public List<User> getCommonFriends(int idUser1, int idUser2) {
        String sql = "select id_friend from relationship where id = ? and id_friend in " +
                "(select id_friend from relationship where id = ?)";
        List<Integer> friends = jdbcTemplate.queryForList(sql, Integer.class, idUser1, idUser2);
        return friends.stream()
                .map(userStorageDao::findById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllFriends(int idUser) {
        String sql = "select id_friend from relationship where id = ?";
        List<Integer> friends = jdbcTemplate.queryForList(sql, Integer.class, idUser);
        return friends.stream()
                .map(userStorageDao::findById)
                .collect(Collectors.toList());
    }
}
