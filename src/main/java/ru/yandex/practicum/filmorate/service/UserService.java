package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService {

	private final UserStorage userStorage;


	@Autowired
	public UserService(UserStorage userStorage) {
		this.userStorage = userStorage;

	}



	public User addFriend(int userId, int friendId) {
		User user = userStorage.getUserById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь не найден");
		}
		User friend = userStorage.getUserById(friendId);
		if (friend == null) {
			throw new NotFoundException("Добавляемый в друзья пользователь не найден");
		}
		user.getFriends().add(friendId);
		friend.getFriends().add(userId);
		return user;

	}

	public User removeFriend(int userId, int friendId) {
		User user = userStorage.getUserById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь не найден");
		}
		User friend = userStorage.getUserById(friendId);
		if (friend == null) {
			throw new NotFoundException("Удаляемый из друзей пользователь не найден");
		}
		user.getFriends().remove(friendId);
		friend.getFriends().remove(userId);
		return user;

	}

	public Set<User> showMutualFriends(int userId, int targetId) {
		User user = userStorage.getUserById(userId);
		User target = userStorage.getUserById(targetId);
		return target.getFriends()
				.stream()
				.filter(friendId -> user.getFriends().contains(friendId))
				.map(userStorage::getUserById)
				.collect(Collectors.toSet());
	}

	public Set<User> getFriends(int userId) {
		User user = userStorage.getUserById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь не найден");
		}
		return user.getFriends()
				.stream()
				.map(userStorage::getUserById)
				.collect(Collectors.toSet());
	}
}
