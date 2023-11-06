package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmStorageTest {
    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    Film filmStandard = new Film();
    Film filmStandardWithId = new Film();
    Film filmInvalidName = new Film();
    Film filmInvalidNameWithId = new Film();
    Film filmInvalidDescription = new Film();
    Film filmInvalidDescriptionWithId = new Film();
    Film filmInvalidData = new Film();
    Film filmInvalidDataWithId = new Film();
    Film filmInvalidDuration = new Film();
    Film filmInvalidDurationWithId = new Film();

    @BeforeEach
    public void beforeEach() {
        filmStandard.setName("Терминал");
        filmStandard.setDescription("Хороший фильм");
        filmStandard.setReleaseDate(LocalDate.of(2004, 10, 14));
        filmStandard.setDuration(120);
        filmStandardWithId.setId(1);
        filmStandardWithId.setName("Терминал");
        filmStandardWithId.setDescription("Хороший фильм");
        filmStandardWithId.setReleaseDate(LocalDate.of(2004, 10, 14));
        filmStandardWithId.setDuration(120);
        filmInvalidName.setName("");
        filmInvalidName.setDescription("Хороший фильм");
        filmInvalidName.setReleaseDate(LocalDate.of(2004, 10, 14));
        filmInvalidName.setDuration(120);
        filmInvalidNameWithId.setId(1);
        filmInvalidNameWithId.setName("");
        filmInvalidNameWithId.setDescription("Хороший фильм");
        filmInvalidNameWithId.setReleaseDate(LocalDate.of(2004, 10, 14));
        filmInvalidNameWithId.setDuration(120);
        filmInvalidDescription.setName("Терминал");
        filmInvalidDescription.setDescription("228 символов" +
                "                                                                                                            " +
                "                                                                                                            ");
        filmInvalidDescription.setReleaseDate(LocalDate.of(2004, 10, 14));
        filmInvalidDescription.setDuration(120);
        filmInvalidDescriptionWithId.setId(1);
        filmInvalidDescriptionWithId.setName("Терминал");
        filmInvalidDescriptionWithId.setDescription("228 символов" +
                "                                                                                                            " +
                "                                                                                                            ");
        filmInvalidDescriptionWithId.setReleaseDate(LocalDate.of(2004, 10, 14));
        filmInvalidDescriptionWithId.setDuration(120);
        filmInvalidData.setName("Терминал");
        filmInvalidData.setDescription("Хороший фильм");
        filmInvalidData.setReleaseDate(LocalDate.of(1800, 10, 14));
        filmInvalidData.setDuration(120);
        filmInvalidDataWithId.setId(1);
        filmInvalidDataWithId.setName("Терминал");
        filmInvalidDataWithId.setDescription("Хороший фильм");
        filmInvalidDataWithId.setReleaseDate(LocalDate.of(1800, 10, 14));
        filmInvalidDataWithId.setDuration(120);
        filmInvalidDuration.setName("Терминал");
        filmInvalidDuration.setDescription("Хороший фильм");
        filmInvalidDuration.setReleaseDate(LocalDate.of(2004, 10, 14));
        filmInvalidDuration.setDuration(-120);
        filmInvalidDurationWithId.setId(1);
        filmInvalidDurationWithId.setName("Терминал");
        filmInvalidDurationWithId.setDescription("Хороший фильм");
        filmInvalidDurationWithId.setReleaseDate(LocalDate.of(2004, 10, 14));
        filmInvalidDurationWithId.setDuration(-120);
    }

    @Test
    void getFilm() {
        filmStorage.create(filmStandard);
        assertFalse(filmStorage.findAll().isEmpty());
        assertEquals(filmStorage.findAll().size(), 1);
    }

    @Test
    void postFilmStandard() {
        filmStorage.create(filmStandard);
        assertEquals(filmStandard.getId(), 1);
    }

    @Test
    void postFilmInvalidName() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmStorage.create(filmInvalidName);
            }
        });
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void postFilmInvalidDescription() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmStorage.create(filmInvalidDescription);
            }
        });
        assertEquals("Описание не может быть пустым или превышать 200 символов", exception.getMessage());
    }

    @Test
    void postFilmInvalidData() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmStorage.create(filmInvalidData);
            }
        });
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void postFilmInvalidDuration() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmStorage.create(filmInvalidDuration);
            }
        });
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    void putFilmStandard() {
        filmStorage.create(filmStandard);
        Film film = filmStorage.update(filmStandardWithId);
        assertEquals(film, filmStandardWithId);
    }

    @Test
    void putFilmInvalidName() {
        filmStorage.create(filmStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmStorage.update(filmInvalidNameWithId);
            }
        });
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void putFilmInvalidDescription() {
        filmStorage.create(filmStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmStorage.update(filmInvalidDescriptionWithId);
            }
        });
        assertEquals("Описание не может быть пустым или превышать 200 символов", exception.getMessage());
    }

    @Test
    void putFilmInvalidData() {
        filmStorage.create(filmStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmStorage.update(filmInvalidDataWithId);
            }
        });
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void putFilmInvalidDuration() {
        filmStorage.create(filmStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmStorage.update(filmInvalidDurationWithId);
            }
        });
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    void putFilmWithoutId() {
        final NotFoundException exception = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmStorage.update(filmStandard);
            }
        });
        assertEquals("Id не найден, проверьте id", exception.getMessage());
    }
}