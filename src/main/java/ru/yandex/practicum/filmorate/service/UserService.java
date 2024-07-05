package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;


@Service
public class UserService {

	private final UserDbStorage userStorage;


	@Autowired
	public UserService(UserDbStorage userStorage) {
		this.userStorage = userStorage;

	}

	public void addFriend(int userId, int friendId) {
		try {
			userStorage.addFriend(userId, friendId);
		} catch (RuntimeException e) {
			throw new NotFoundException("Пользователь не найден");
		}

	}

	public void removeFriend(int userId, int friendId) {
		try {
			getUserById(userId);
			getUserById(friendId);
		} catch (RuntimeException e) {
			throw new NotFoundException("Пользователь не найден");
		}
		userStorage.removeFriend(userId, friendId);

	}

	public Collection<User> showMutualFriends(int userId, int targetId) {
		return userStorage.showMutualFriends(userId, targetId);
	}

	public Collection<User> getFriends(int userId) {
		try {
			getUserById(userId);
		} catch (RuntimeException e) {
			throw new NotFoundException("Пользователь не найден");
		}
		return userStorage.getFriends(userId);
	}

	public User create(User user) {
		return userStorage.create(user);
	}

	public Optional<User> getUserById(int id) {
		return userStorage.getUserById(id);
	}

	public User update(User user) {
		try {
			return userStorage.update(user);
		} catch (InternalServerException e) {
			throw new NotFoundException("Пользователь не найден");
		}
	}

	public Collection<User> getAllUsers() {
		return userStorage.getAllUsers();
	}
}
