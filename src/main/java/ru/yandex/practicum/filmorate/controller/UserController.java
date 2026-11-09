package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    public User create(@RequestBody User user) {
        validate(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(nextId++);
        users.put(user.getId(), user);

        log.info("Создан пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        validate(user);

        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с id {} не найден", user.getId());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Пользователь не найден"
            );
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);

        log.info("Обновлён пользователь: {}", user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    private void validate(User user) {
        if (user == null) {
            log.warn("Передан пустой объект пользователя");
            throw new ValidationException("Пользователь не может быть null");
        }

        if (user.getEmail() == null
                || user.getEmail().isBlank()
                || !user.getEmail().contains("@")) {
            log.warn("Некорректная электронная почта");
            throw new ValidationException(
                    "Электронная почта не может быть пустой и должна содержать @"
            );
        }

        if (user.getLogin() == null
                || user.getLogin().isBlank()
                || user.getLogin().contains(" ")) {
            log.warn("Некорректный логин пользователя");
            throw new ValidationException(
                    "Логин не может быть пустым или содержать пробелы"
            );
        }

        if (user.getBirthday() == null
                || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата рождения пользователя");
            throw new ValidationException(
                    "Дата рождения не может быть в будущем"
            );
        }
    }
}