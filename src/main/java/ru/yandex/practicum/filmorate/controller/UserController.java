package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;

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
				.max(Integer::compareTo)
				.orElse(0);
		return ++currentMaxId;
	}

	@GetMapping
	public Collection<User> getAllUsers() {
		return users.values();
	}

	@PostMapping
	public User create(@Validated({BasicInfo.class, AdvanceInfo.class}) @RequestBody User user) {
		user.setId(getNextId());
		if (user.getName() == null) {
			user.setName(user.getLogin());
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
	public User update(@Validated(AdvanceInfo.class) @RequestBody User newUser) {
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
			} else if (newUser.getEmail() != null && !newUser.getLogin().isEmpty()) {
				oldUser.setEmail(newUser.getEmail());
				log.info("E-mail обновлён");
			}
			if (users.values()
					.stream()
					.anyMatch(getUser -> getUser.getLogin().equals(newUser.getLogin())) && !oldUser.getLogin().equals(newUser.getLogin())) {
				log.warn("Указан использующийся логин");
				throw new ValidationException("Такой логин уже используется");
			} else if (newUser.getLogin() != null && !newUser.getLogin().isEmpty()) {
				oldUser.setLogin(newUser.getLogin());
				log.info("Логин обновлён");
			}
			if (newUser.getName() != null) {
				oldUser.setName(newUser.getName());
				log.info("Имя обновлено");
			}
			if (newUser.getBirthday() != null) {
				oldUser.setBirthday(newUser.getBirthday());
				log.info("День рождения обновлён");
			}
			return oldUser;
		}
		log.warn("Указан несуществующий id");
		throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
	}
}
