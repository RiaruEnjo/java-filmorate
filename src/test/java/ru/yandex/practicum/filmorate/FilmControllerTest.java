package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController controller;
    private Film film;

    @BeforeEach
    void setUp() {
        controller = new FilmController();

        film = new Film();
        film.setName("Титаник");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(1997, 12, 19));
        film.setDuration(194);
    }

    @Test
    void shouldCreateValidFilm() {
        Film created = controller.create(film);

        assertEquals(1, created.getId());
        assertEquals(1, controller.findAll().size());
    }

    @Test
    void shouldRejectEmptyName() {
        film.setName("");

        assertThrows(ValidationException.class,
                () -> controller.create(film));
    }

    @Test
    void shouldRejectNullName() {
        film.setName(null);

        assertThrows(ValidationException.class,
                () -> controller.create(film));
    }

    @Test
    void shouldRejectLongDescription() {
        film.setDescription("a".repeat(201));

        assertThrows(ValidationException.class,
                () -> controller.create(film));
    }

    @Test
    void shouldAcceptDescriptionWith200Characters() {
        film.setDescription("a".repeat(200));

        assertDoesNotThrow(() -> controller.create(film));
    }

    @Test
    void shouldRejectEarlyReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        assertThrows(ValidationException.class,
                () -> controller.create(film));
    }

    @Test
    void shouldAcceptBoundaryReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        assertDoesNotThrow(() -> controller.create(film));
    }

    @Test
    void shouldRejectZeroDuration() {
        film.setDuration(0);

        assertThrows(ValidationException.class,
                () -> controller.create(film));
    }

    @Test
    void shouldRejectNullFilm() {
        assertThrows(ValidationException.class,
                () -> controller.create(null));
    }
}