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
			User user = new User();
			long userId = rs.getLong("id");
			user.setId(userId);
			user.setEmail(rs.getString("email"));
			user.setLogin(rs.getString("login"));
			user.setBirthday(rs.getDate("birthday").toLocalDate());
			user.setName(rs.getString("name"));
			userIdToUser.put(userId, user);
			}
		return new ArrayList<>(userIdToUser.values());
	}

}
