package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

	private final Map<Long, User> users = new HashMap<>();
	private final Set<String> emails = new HashSet<>();
	private final Set<String> logins = new HashSet<>();


	private long getNextId() {
		long currentMaxId = users.keySet()
				.stream()
				.max(Long::compareTo)
				.orElse(0L);
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
		if (emails.contains(user.getEmail())) {
			throw new ValidationException("Такой e-mail уже используется");
		}
		if (logins.contains(user.getLogin())) {
			throw new ValidationException("Такой логин уже используется");
		}
		users.put(user.getId(), user);
		logins.add(user.getLogin());
		emails.add(user.getEmail());
		return user;
	}

	@Override
	public User update(User newUser) {
		if (newUser.getId() == null) {
			throw new ValidationException("Id должен быть указан");
		}
		if (users.containsKey(newUser.getId())) {
			User oldUser = users.get(newUser.getId());
			if (emails.contains(newUser.getEmail())) {
				throw new ValidationException("Такой e-mail уже используется");
			} else if (newUser.getEmail() != null) {
				emails.remove(oldUser.getEmail());
				emails.add(newUser.getEmail());
				oldUser.setEmail(newUser.getEmail());

			}
			if (logins.contains(newUser.getLogin())) {
				throw new ValidationException("Такой логин уже используется");
			} else if (newUser.getLogin() != null) {
				logins.remove(oldUser.getLogin());
				logins.add(newUser.getLogin());
				oldUser.setLogin(newUser.getLogin());
			}
			if (newUser.getName() != null) {
				oldUser.setName(newUser.getName());
			}
			if (newUser.getBirthday() != null) {
				oldUser.setBirthday(newUser.getBirthday());
			}
			return oldUser;
		}
		throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");

	}

	@Override
	public User getUserById(long userId) {
		return users.get(userId);
	}
}
