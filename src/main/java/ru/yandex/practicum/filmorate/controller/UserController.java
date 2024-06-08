package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;

import java.util.Collection;
import java.util.Set;


@RestController
public class UserController {

	private final UserService userService;
	private final UserStorage userStorage;

	@Autowired
	public UserController(UserService userService, UserStorage userStorage) {
		this.userService = userService;
		this.userStorage = userStorage;
	}

	@GetMapping("/users")
	public Collection<User> getAllUsers() {
		return userStorage.getAllUsers();
	}


	@PostMapping("/users")
	public User create(@Validated({BasicInfo.class, AdvanceInfo.class}) @RequestBody User user) {
		return userStorage.create(user);
	}


	@PutMapping("/users")
	public User update(@Validated(AdvanceInfo.class) @RequestBody User newUser) {
		return userStorage.update(newUser);
	}

	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable("id") int id) {
		return userStorage.getUserById(id);
	}

	@PutMapping("/users/{id}/friends/{friendId}")
	public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
		userService.addFriend(id, friendId);
	}

	@DeleteMapping("/users/{id}/friends/{friendId}")
	public void removeFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
		userService.removeFriend(id, friendId);
	}

	@GetMapping("/users/{id}/friends")
	public Set<User> getFriends(@PathVariable("id") int id) {
		return userService.getFriends(id);
	}

	@GetMapping("/users/{id}/friends/common/{otherId}")
	public Set<User> showMutualFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
		return userService.showMutualFriends(id, otherId);
	}

}
