package ru.yandex.practicum.filmorate.dal.extractors;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserExtractor implements ResultSetExtractor<List<User>> {
	@Override
	public List<User> extractData(ResultSet rs) throws SQLException {
		Map<Long, User> userIdToUser = new HashMap<>();
		while (rs.next()) {
			long userId = rs.getLong("id");
			if (userIdToUser.containsKey(userId)) {
				long friendId = rs.getLong("friend_id");
				if (friendId != 0) {
					userIdToUser.get(userId).getFriends().add(friendId);
				}
			} else {
				User user = new User();
				user.setId(userId);
				user.setEmail(rs.getString("email"));
				user.setLogin(rs.getString("login"));
				user.setBirthday(rs.getDate("birthday").toLocalDate());
				user.setName(rs.getString("name"));
				long friendId = rs.getLong("friend_id");
				if (friendId != 0) {
					user.getFriends().add(friendId);
				}
				userIdToUser.put(userId, user);
			}

			// проверяем, есть ли пользователь в userIdToUser
			// если нет - создаем, если есть то достаем его из мапы и в его список друзей добавляем int friendId = rs.getInt("friend_id");
			//...
		}

		// Преобразуем Map в список
		return new ArrayList<>(userIdToUser.values());
	}

}
