package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    FilmController filmController = new FilmController();
    Film filmStandard = Film.builder().name("Терминал")
            .description("Хороший фильм")
            .releaseDate(LocalDate.of(2004, 10, 14))
            .duration(120).build();
    Film filmStandardWithId = Film.builder().id(1)
            .name("Терминал")
            .description("Хороший фильм")
            .releaseDate(LocalDate.of(2004, 10, 14))
            .duration(120).build();
    Film filmInvalidName = Film.builder().name("")
            .description("Хороший фильм")
            .releaseDate(LocalDate.of(2004, 10, 14))
            .duration(120).build();
    Film filmInvalidNameWithId = Film.builder().id(1)
            .name("")
            .description("Хороший фильм")
            .releaseDate(LocalDate.of(2004, 10, 14))
            .duration(120).build();
    Film filmInvalidDescription = Film.builder().name("Терминал")
            .description("228 символов" +
                    "                                                                                                            " +
                    "                                                                                                            ")
            .releaseDate(LocalDate.of(2004, 10, 14))
            .duration(120).build();
    Film filmInvalidDescriptionWithId = Film.builder().id(1)
            .name("Терминал")
            .description("228 символов" +
                    "                                                                                                            " +
                    "                                                                                                            ")
            .releaseDate(LocalDate.of(2004, 10, 14))
            .duration(120).build();
    Film filmInvalidData = Film.builder().name("Терминал")
            .description("Хороший фильм")
            .releaseDate(LocalDate.of(1800, 10, 14))
            .duration(120).build();
    Film filmInvalidDataWithId = Film.builder().id(1)
            .name("Терминал")
            .description("Хороший фильм")
            .releaseDate(LocalDate.of(1800, 10, 14))
            .duration(120).build();
    Film filmInvalidDuration = Film.builder().name("Терминал")
            .description("Хороший фильм")
            .releaseDate(LocalDate.of(2004, 10, 14))
            .duration(-120).build();
    Film filmInvalidDurationWithId = Film.builder().id(1)
            .name("Терминал")
            .description("Хороший фильм")
            .releaseDate(LocalDate.of(2004, 10, 14))
            .duration(-120).build();

    @Test
    void getFilm() {
        filmController.create(filmStandard);
        assertFalse(filmController.findAll().isEmpty());
        assertEquals(filmController.findAll().size(), 1);
    }

    @Test
    void postFilmStandard() {
        filmController.create(filmStandard);
        assertEquals(filmStandard.getId(), 1);
    }

    @Test
    void postFilmInvalidName() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.create(filmInvalidName);
            }
        });
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void postFilmInvalidDescription() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.create(filmInvalidDescription);
            }
        });
        assertEquals("Описание не может быть пустым или превышать 200 символов", exception.getMessage());
    }

    @Test
    void postFilmInvalidData() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.create(filmInvalidData);
            }
        });
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void postFilmInvalidDuration() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.create(filmInvalidDuration);
            }
        });
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    void putFilmStandard() {
        filmController.create(filmStandard);
        Film film = filmController.update(filmStandardWithId);
        assertEquals(film, filmStandardWithId);
    }

    @Test
    void putFilmInvalidName() {
        filmController.create(filmStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.update(filmInvalidNameWithId);
            }
        });
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void putFilmInvalidDescription() {
        filmController.create(filmStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.update(filmInvalidDescriptionWithId);
            }
        });
        assertEquals("Описание не может быть пустым или превышать 200 символов", exception.getMessage());
    }

    @Test
    void putFilmInvalidData() {
        filmController.create(filmStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.update(filmInvalidDataWithId);
            }
        });
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void putFilmInvalidDuration() {
        filmController.create(filmStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.update(filmInvalidDurationWithId);
            }
        });
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    void putFilmWithoutId() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.update(filmStandard);
            }
        });
        assertEquals("Id не найден, проверьте id", exception.getMessage());
    }
}