package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;
    private User user;

    @BeforeEach
    void setUp() {
        controller = new UserController();

        user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("Тест");
        user.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void shouldCreateValidUser() {
        User created = controller.create(user);

        assertEquals(1, created.getId());
        assertEquals(1, controller.findAll().size());
    }

    @Test
    void shouldRejectEmptyEmail() {
        user.setEmail("");

        assertThrows(ValidationException.class,
                () -> controller.create(user));
    }

    @Test
    void shouldRejectEmailWithoutAt() {
        user.setEmail("incorrect-email");

        assertThrows(ValidationException.class,
                () -> controller.create(user));
    }

    @Test
    void shouldRejectLoginWithSpace() {
        user.setLogin("test login");

        assertThrows(ValidationException.class,
                () -> controller.create(user));
    }

    @Test
    void shouldRejectEmptyLogin() {
        user.setLogin("");

        assertThrows(ValidationException.class,
                () -> controller.create(user));
    }

    @Test
    void shouldUseLoginWhenNameIsEmpty() {
        user.setName("");

        User created = controller.create(user);

        assertEquals("testlogin", created.getName());
    }

    @Test
    void shouldRejectFutureBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class,
                () -> controller.create(user));
    }

    @Test
    void shouldAcceptTodayAsBirthday() {
        user.setBirthday(LocalDate.now());

        assertDoesNotThrow(() -> controller.create(user));
    }

    @Test
    void shouldRejectNullUser() {
        assertThrows(ValidationException.class,
                () -> controller.create(null));
    }
}