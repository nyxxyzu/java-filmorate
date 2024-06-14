package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

	Collection<User> getAllUsers();

	User create(User user);

	User update(User newUser);

	User getUserById(int userId);
}
