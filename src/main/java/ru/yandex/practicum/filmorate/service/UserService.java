package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


@Service
public class UserService {

	private final UserDbStorage userStorage;


	@Autowired
	public UserService(UserDbStorage userStorage) {
		this.userStorage = userStorage;

	}

	public void addFriend(int userId, int friendId) {
		userStorage.addFriend(userId, friendId);

	}

	public void removeFriend(int userId, int friendId) {
		userStorage.removeFriend(userId, friendId);

	}

	public Collection<User> showMutualFriends(int userId, int targetId) {
		return userStorage.showMutualFriends(userId, targetId);
	}

	public Collection<User> getFriends(int userId) {
		return userStorage.getFriends(userId);
	}

	public User create(User user) {
		return userStorage.create(user);
	}

	public User getUserById(int id) {
		return userStorage.getUserById(id);
	}

	public User update(User user) {

		return userStorage.update(user);
	}

	public Collection<User> getAllUsers() {
		return userStorage.getAllUsers();
	}
}
