package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film extends Entity {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private Set<Integer> likes = new HashSet<>();
    private int rate = 0;

    public void addLikes(int id) {
        likes.add(id);
        rate = likes.size();
    }

    public void deleteLikes(int id) {
        likes.remove(id);
        rate = likes.size();
    }

}
