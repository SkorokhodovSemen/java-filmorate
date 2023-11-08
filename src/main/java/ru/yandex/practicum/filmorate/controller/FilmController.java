package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController extends EntityController<Film, FilmService> {
    @Autowired
    public FilmController(FilmService entityService) {
        super(entityService);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLikes(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return entityService.addLikes(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return entityService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return entityService.getPopularFilms(count);
    }
}
