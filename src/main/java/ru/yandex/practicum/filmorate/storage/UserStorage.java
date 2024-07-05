package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

	Collection<User> getAllUsers();

	User create(User user);

	User update(User newUser);

	Optional<User> getUserById(long userId);
}
