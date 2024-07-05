package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {

	private Long id;
	@NotEmpty(message = "E-mail не должен быть пуст", groups = BasicInfo.class)
	@Email(message = "Неправильный формат e-mail.", groups = AdvanceInfo.class)
	private String email;
	@NotEmpty(message = "Логин не должен быть пуст", groups = BasicInfo.class)
	@Pattern(regexp = "^([A-z])*\\S*$", message = "Логин не должен содержать пробелов.", groups = AdvanceInfo.class)
	private String login;
	private String name;
	@Past(groups = AdvanceInfo.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;

}
