package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;

import java.util.Collection;

@Slf4j
@RestController
public class UserController {

	private final UserService userService;


	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;

	}

	@GetMapping("/users")
	public Collection<User> getAllUsers() {
		log.info(userService.getAllUsers().toString());
		return userService.getAllUsers();
	}


	@PostMapping("/users")
	public User create(@Validated({BasicInfo.class, AdvanceInfo.class}) @RequestBody User user) {
		log.info(user.toString());
		return userService.create(user);
	}


	@PutMapping("/users")
	public User update(@Validated(AdvanceInfo.class) @RequestBody User newUser) {
		log.info(newUser.toString());
		return userService.update(newUser);
	}

	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable("id") int id) {
		log.info(userService.getUserById(id).toString());
		return userService.getUserById(id);
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
	public Collection<User> getFriends(@PathVariable("id") int id) {
		log.info(userService.getFriends(id).toString());
		return userService.getFriends(id);
	}

	@GetMapping("/users/{id}/friends/common/{otherId}")
	public Collection<User> showMutualFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
		log.info(userService.showMutualFriends(id, otherId).toString());
		return userService.showMutualFriends(id, otherId);
	}

}
