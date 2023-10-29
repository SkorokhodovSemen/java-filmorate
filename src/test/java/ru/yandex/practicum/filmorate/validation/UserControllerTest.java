package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
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
    User userStandard = new User();
    User userStandardWithId = new User();
    User userInvalidEmail = new User();
    User userInvalidEmailWithId = new User();
    User userInvalidLogin = new User();
    User userInvalidLoginWithId = new User();
    User userInvalidBirthday = new User();
    User userInvalidBirthdayWithId = new User();
    User userWithoutName = new User();

    @BeforeEach
    public void beforeEach() {
        userStandard.setName("Semen");
        userStandard.setEmail("yandex@yandex.ru");
        userStandard.setLogin("SuperSam");
        userStandard.setBirthday(LocalDate.of(1996, 10, 16));
        userStandardWithId.setId(1);
        userStandardWithId.setName("Semen");
        userStandardWithId.setEmail("yandex@yandex.ru");
        userStandardWithId.setLogin("SeriousSam");
        userStandardWithId.setBirthday(LocalDate.of(1996, 10, 16));
        userInvalidEmail.setName("Semen");
        userInvalidEmail.setEmail("yandex.ru");
        userInvalidEmail.setLogin("SuperSam");
        userInvalidEmail.setBirthday(LocalDate.of(1996, 10, 16));
        userInvalidEmailWithId.setId(1);
        userInvalidEmailWithId.setName("Semen");
        userInvalidEmailWithId.setEmail("yandex.ru");
        userInvalidEmailWithId.setLogin("SuperSam");
        userInvalidEmailWithId.setBirthday(LocalDate.of(1996, 10, 16));
        userInvalidLogin.setName("Semen");
        userInvalidLogin.setEmail("yandex@yandex.ru");
        userInvalidLogin.setLogin(" ");
        userInvalidLogin.setBirthday(LocalDate.of(1996, 10, 16));
        userInvalidLoginWithId.setId(1);
        userInvalidLoginWithId.setName("Semen");
        userInvalidLoginWithId.setEmail("yandex@yandex.ru");
        userInvalidLoginWithId.setLogin(" ");
        userInvalidLoginWithId.setBirthday(LocalDate.of(1996, 10, 16));
        userInvalidBirthday.setName("Semen");
        userInvalidBirthday.setEmail("yandex@yandex.ru");
        userInvalidBirthday.setLogin("SuperSam");
        userInvalidBirthday.setBirthday(LocalDate.of(2216, 10, 16));
        userInvalidBirthdayWithId.setId(1);
        userInvalidBirthdayWithId.setName("Semen");
        userInvalidBirthdayWithId.setEmail("yandex@yandex.ru");
        userInvalidBirthdayWithId.setLogin("SuperSam");
        userInvalidBirthdayWithId.setBirthday(LocalDate.of(2216, 10, 16));
        userStandard.setName("Semen");
        userWithoutName.setEmail("yandex@yandex.ru");
        userWithoutName.setLogin("SuperSam");
        userWithoutName.setBirthday(LocalDate.of(1996, 10, 16));
    }

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
