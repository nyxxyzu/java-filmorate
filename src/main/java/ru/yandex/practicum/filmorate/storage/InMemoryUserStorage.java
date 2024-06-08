package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

	private final Map<Integer, User> users = new HashMap<>();


	private int getNextId() {
		int currentMaxId = users.keySet()
				.stream()
				.max(Integer::compareTo)
				.orElse(0);
		return ++currentMaxId;
	}

	@Override
	public Collection<User> getAllUsers() {
		return users.values();
	}

	@Override
	public User create(User user) {
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

	@Override
	public User update(User newUser) {
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
		throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");

	}

	@Override
	public User getUserById(int userId) {
		return users.get(userId);
	}
}
