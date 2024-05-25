package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

	private final Map<Integer, User> users = new HashMap<>();

	private int getNextId() {
		int currentMaxId = users.keySet()
				.stream()
				.mapToInt(id -> id)
				.max()
				.orElse(0);
		return ++currentMaxId;
	}

	@GetMapping
	public Collection<User> getAllUsers() {
		return users.values();
	}

	@PostMapping
	public User create(@Valid @RequestBody User user) {
		user.setId(getNextId());
		if (user.getName() == null) {
			user.setName(user.getLogin());
		}
		if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
			log.warn("Указана дата рождения в будущем.");
			throw new ValidationException("Дата рождения не может быть в будущем.");
		}
		if (users.values()
				.stream()
				.anyMatch(getUser -> getUser.getEmail().equals(user.getEmail()))) {
			log.warn("Указан использующийся e-mail");
			throw new ValidationException("Такой e-mail уже используется");
		}
		if (users.values()
				.stream()
				.anyMatch(getUser -> getUser.getLogin().equals(user.getLogin()))) {
			log.warn("Указан использующийся логин");
			throw new ValidationException("Такой логин уже используется");
		}
		users.put(user.getId(), user);
		log.info("Пользователь создан");
		return user;
	}

	@PutMapping
	public User update(@Valid @RequestBody User newUser) {
		if (newUser.getId() == null) {
			log.warn("Id не указан");
			throw new ValidationException("Id должен быть указан");
		}
		if (users.containsKey(newUser.getId())) {
			User oldUser = users.get(newUser.getId());
			if (users.values()
					.stream()
					.anyMatch(getUser -> getUser.getEmail().equals(newUser.getEmail())) && !oldUser.getEmail().equals(newUser.getEmail())) {
				log.warn("Указан использующийся e-mail");
				throw new ValidationException("Такой e-mail уже используется");
			} else {
				oldUser.setEmail(newUser.getEmail());
				log.info("E-mail обновлён");
			}
			if (users.values()
					.stream()
					.anyMatch(getUser -> getUser.getLogin().equals(newUser.getLogin())) && !oldUser.getLogin().equals(newUser.getLogin())) {
				log.warn("Указан использующийся логин");
				throw new ValidationException("Такой логин уже используется");
			} else {
				oldUser.setLogin(newUser.getLogin());
				log.info("Логин обновлён");
			}
			if (newUser.getName() != null) {
				oldUser.setName(newUser.getName());
				log.info("Имя обновлено");
			}
			if (newUser.getBirthday() != null) {
				if (newUser.getBirthday().isAfter(LocalDate.now())) {
					throw new ValidationException("Дата рождения не может быть в будущем");
				} else {
					oldUser.setBirthday(newUser.getBirthday());
					log.info("День рождения обновлён");
				}
			}
			return oldUser;
		}
			log.warn("Указан несуществующий id");
			throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
	}
}
