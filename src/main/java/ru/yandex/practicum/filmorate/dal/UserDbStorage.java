package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Repository
@Primary
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {

	private static final String GET_ALL_QUERY = "SELECT u.*, f.friend_id " +
			"FROM users AS u LEFT JOIN friends AS f ON u.id = f.user_id";
	private static final String GET_BY_ID_QUERY = "SELECT u.*, f.friend_id " +
			"FROM users AS u LEFT JOIN friends AS f ON u.id = f.user_id WHERE u.id = ?";
	private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_QUERY = "UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?";
	private static final String ADD_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id) SELECT ?, ? " +
			"WHERE NOT EXISTS (SELECT * FROM friends WHERE user_id = ? AND friend_id = ?)";
	private static final String UPDATE_STATUS_QUERY = "UPDATE friends SET accepted = true WHERE user_id = ? AND friend_id = ?";
	private static final String REMOVE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
	private static final String GET_MUTUALS_QUERY = "SELECT u.*, f.friend_id FROM users AS u LEFT JOIN friends AS f ON u.id = f.user_id " +
			"WHERE u.id IN (SELECT friend_id FROM friends WHERE user_id = ?)" +
			"AND u.id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
	private static final String GET_ALL_FRIENDS_QUERY = "SELECT u.*, f.friend_id FROM users AS u LEFT JOIN friends AS f ON u.id = f.user_id " +
			"WHERE u.id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
	private static final String CLEAR_USERS_QUERY = "DELETE FROM users";

	@Autowired
	public UserDbStorage(JdbcTemplate jdbc, ResultSetExtractor<List<User>> extractor) {
		super(jdbc, extractor, User.class);
	}

	@Override
	public Collection<User> getAllUsers() {
		return findMany(GET_ALL_QUERY);

	}

	@Override
	public User create(User user) {
		long id = insert(
				INSERT_QUERY,
				user.getEmail(),
				user.getLogin(),
				user.getName(),
				Date.valueOf(user.getBirthday())
		);
		user.setId(id);
		return user;
	}
	@Override
	public User update(User newUser) {
		try {
			update(
					UPDATE_QUERY,
					newUser.getLogin(),
					newUser.getEmail(),
					newUser.getName(),
					newUser.getBirthday(),
					newUser.getId()
			);
		} catch (InternalServerException e) {
			throw new NotFoundException("Пользователь не найден");
		}
		return newUser;
	}

	@Override
	public User getUserById(long userId) {
		return findOne(GET_BY_ID_QUERY, userId);
	}

	public void addFriend(long userId, long friendId) {
		try {
			getUserById(userId);
			getUserById(friendId);
		} catch (RuntimeException e) {
			throw new NotFoundException("Пользователь не найден");
		}
		noPkInsert(
				ADD_FRIEND_QUERY,
				userId,
				friendId,
				userId,
				friendId
		);
		if (getUserById(friendId).getFriends().contains(userId)) {
			update(UPDATE_STATUS_QUERY, userId, friendId);
			update(UPDATE_STATUS_QUERY, friendId, userId);
		}

	}

	public void removeFriend(int userId, int friendId) {
		try {
			getUserById(userId);
			getUserById(friendId);
		} catch (RuntimeException e) {
			throw new NotFoundException("Пользователь не найден");
		}
		delete(
				REMOVE_FRIEND_QUERY,
				userId,
				friendId
		);
	}

	public Collection<User> showMutualFriends(int userId, int targetId) {
		return findMany(GET_MUTUALS_QUERY, userId, targetId);

	}

	public Collection<User> getFriends(int userId) {
		try {
			getUserById(userId);
		} catch (RuntimeException e) {
			throw new NotFoundException("Пользователь не найден");
		}
		return findMany(GET_ALL_FRIENDS_QUERY, userId);
	}

	public void clearUsers() {
		delete(CLEAR_USERS_QUERY);
	}

}
