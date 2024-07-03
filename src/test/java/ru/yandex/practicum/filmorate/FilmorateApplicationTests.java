package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.dal.extractors.FilmExtractor;
import ru.yandex.practicum.filmorate.dal.extractors.UserExtractor;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class, UserExtractor.class, FilmDbStorage.class, FilmExtractor.class})
class FilmorateApplicationTests {

	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	@Test
	void testGetAllUsers() {
		User user1 = new User();
		User user2 = new User();
		user1.setLogin("login2");
		user1.setEmail("email2@mail.com");
		user1.setBirthday(LocalDate.of(1996, 6, 5));
		user2.setLogin("login3");
		user2.setEmail("email3@mail.com");
		user2.setBirthday(LocalDate.of(1995, 6, 5));
		userStorage.create(user1);
		userStorage.create(user2);
		assertEquals(2, userStorage.getAllUsers().size());
	}

	@Test
	void testGetUser() {
		User user3 = new User();
		user3.setLogin("login1");
		user3.setEmail("email1@mail.com");
		user3.setBirthday(LocalDate.of(1997, 6, 5));
		userStorage.create(user3);
		assertThat(userStorage.getUserById(3)).hasFieldOrPropertyWithValue("id",3L);

	}

	@Test
	void testGetAllFilms() {
		Film film1 = new Film();
		Film film2 = new Film();
		film1.setName("filmname1");
		film1.setDescription("filmdesc1");
		film1.setReleaseDate(LocalDate.of(1995,5,25));
		film1.setDuration(Duration.ofMinutes(150));
		film1.setMpa(new Mpa());
		film2.setName("filmname2");
		film2.setDescription("filmdesc2");
		film2.setReleaseDate(LocalDate.of(1996,5,25));
		film2.setDuration(Duration.ofMinutes(150));
		film2.setMpa(new Mpa());
		filmStorage.create(film1);
		filmStorage.create(film2);
		assertEquals(2, filmStorage.getAllFilms().size());
	}

	@Test
	void testGetFilm() {
		Film film3 = new Film();
		film3.setName("filmname3");
		film3.setDescription("filmdesc3");
		film3.setReleaseDate(LocalDate.of(1996,5,25));
		film3.setDuration(Duration.ofMinutes(150));
		film3.setMpa(new Mpa());
		filmStorage.create(film3);
		assertThat(filmStorage.getFilmById(3)).hasFieldOrPropertyWithValue("id",3L);

	}


	@Test
	void cantCreateFilmWithNoName() {
		Film film = new Film();
		film.setName("");
		Set<ConstraintViolation<Film>> violations = validator.validate(film, BasicInfo.class);
		assertThat(violations.size()).isEqualTo(1);

	}

	@Test
	void cantMakeFilmDescriptionOver200Symbols() {
		Film film = new Film();
		film.setName("Film name");
		film.setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		Set<ConstraintViolation<Film>> violations = validator.validate(film, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void cantMakeReleaseDateBefore1895() {
		Film film = new Film();
		film.setReleaseDate(LocalDate.of(1883,5,5));
		assertThrows(ValidationException.class, () -> {
			filmStorage.create(film);
		});
	}

	@Test
	void durationCantBeNegative() {
		Film film = new Film();
		film.setName("Film name");
		film.setDuration(Duration.ofMinutes(-30));
		Set<ConstraintViolation<Film>> violations = validator.validate(film, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void emailShouldNotBeEmpty() {
		User user = new User();
		user.setEmail("");
		user.setLogin("login");
		Set<ConstraintViolation<User>> violations = validator.validate(user, BasicInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void emailShouldBeCorrect() {
		User user = new User();
		user.setEmail("asdasdasd");
		user.setLogin("login");
		Set<ConstraintViolation<User>> violations = validator.validate(user, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void loginShouldNotBeEmpty() {
		User user = new User();
		user.setEmail("aaaa@yandex.ru");
		user.setLogin("");
		Set<ConstraintViolation<User>> violations = validator.validate(user, BasicInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void loginShouldNotHaveSpaces() {
		User user = new User();
		user.setEmail("aaaa@yandex.ru");
		user.setLogin("login 1");
		Set<ConstraintViolation<User>> violations = validator.validate(user, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void birthDateCanNotBeInFuture() {
		User user = new User();
		user.setEmail("aaaa@yandex.ru");
		user.setLogin("login");
		user.setBirthday(LocalDate.of(2025,5,5));
		Set<ConstraintViolation<User>> violations = validator.validate(user, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}
}
