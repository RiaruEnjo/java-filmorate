package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    public Film create(@RequestBody Film film) {
        validate(film);

        film.setId(nextId++);
        films.put(film.getId(), film);

        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validate(film);

        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Фильм не найден"
            );
        }

        films.put(film.getId(), film);

        log.info("Обновлён фильм: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    private void validate(Film film) {
        if (film == null) {
            log.warn("Передан пустой объект фильма");
            throw new ValidationException("Фильм не может быть null");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription() != null
                && film.getDescription().length() > 200) {
            log.warn("Описание фильма превышает 200 символов");
            throw new ValidationException(
                    "Максимальная длина описания — 200 символов"
            );
        }

        if (film.getReleaseDate() == null
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Некорректная дата релиза фильма");
            throw new ValidationException(
                    "Дата релиза не может быть раньше 28 декабря 1895 года"
            );
        }

        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException(
                    "Продолжительность фильма должна быть положительным числом"
            );
        }
    }
}