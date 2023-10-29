package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    UserController userController = new UserController();
    User userStandard = User.builder().email("yandex@yandex.ru")
            .name("Semen")
            .login("SuperSam")
            .birthday(LocalDate.of(1996, 10, 16))
            .build();
    User userStandardWithId = User.builder().id(1)
            .email("yandex@yandex.ru")
            .name("Semen")
            .login("SeriousSam")
            .birthday(LocalDate.of(1996, 10, 16))
            .build();
    User userInvalidEmail = User.builder().email("yandex.ru")
            .name("Semen")
            .login("SuperSam")
            .birthday(LocalDate.of(1996, 10, 16))
            .build();
    User userInvalidEmailWithId = User.builder().id(1)
            .email("yandex.ru")
            .name("Semen")
            .login("SuperSam")
            .birthday(LocalDate.of(1996, 10, 16))
            .build();
    User userInvalidLogin = User.builder().email("yandex@yandex.ru")
            .name("Semen")
            .login(" ")
            .birthday(LocalDate.of(1996, 10, 16))
            .build();
    User userInvalidLoginWithId = User.builder().id(1)
            .email("yandex@yandex.ru")
            .name("Semen")
            .login(" ")
            .birthday(LocalDate.of(1996, 10, 16))
            .build();
    User userInvalidBirthday = User.builder().email("yandex@yandex.ru")
            .name("Semen")
            .login("SuperSam")
            .birthday(LocalDate.of(2216, 10, 16))
            .build();
    User userInvalidBirthdayWithId = User.builder().id(1)
            .email("yandex@yandex.ru")
            .name("Semen")
            .login("SuperSam")
            .birthday(LocalDate.of(2216, 10, 16))
            .build();
    User userWithoutName = User.builder().email("yandex@yandex.ru")
            .login("SuperSam")
            .birthday(LocalDate.of(1996, 10, 16))
            .build();

    @Test
    void getUser() {
        userController.create(userStandard);
        assertFalse(userController.findAll().isEmpty());
        assertEquals(userController.findAll().size(), 1);
    }

    @Test
    void postUserStandard() {
        userController.create(userStandard);
        assertEquals(userStandard.getId(), 1);
    }

    @Test
    void postUserInvalidEmail() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.create(userInvalidEmail);
            }
        });
        assertEquals("Неверный формат почты или поле не заполнено", exception.getMessage());
    }

    @Test
    void postUserInvalidLogin() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.create(userInvalidLogin);
            }
        });
        assertEquals("Логин не может быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    void postUserInvalidBirthday() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.create(userInvalidBirthday);
            }
        });
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void postUserWithoutName() {
        User user = userController.create(userWithoutName);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void putUserStandard() {
        userController.create(userStandard);
        User user = userController.update(userStandardWithId);
        assertEquals(user.getLogin(), "SeriousSam");
    }

    @Test
    void putUserInvalidEmail() {
        userController.create(userStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.update(userInvalidEmailWithId);
            }
        });
        assertEquals("Неверный формат почты или поле не заполнено", exception.getMessage());
    }

    @Test
    void putUserInvalidLogin() {
        userController.create(userStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.update(userInvalidLoginWithId);
            }
        });
        assertEquals("Логин не может быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    void putUserInvalidBirthday() {
        userController.create(userStandard);
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.update(userInvalidBirthdayWithId);
            }
        });
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void putUserWithoutId() {
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.update(userStandard);
            }
        });
        assertEquals("Id не найден, проверьте id", exception.getMessage());
    }
}
