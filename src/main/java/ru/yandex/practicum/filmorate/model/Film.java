package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.serializers.DurationDeserializer;
import ru.yandex.practicum.filmorate.serializers.DurationSerializer;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
public class Film {

	private Long id;
	@NotEmpty(groups = BasicInfo.class)
	private String name;
	@Size(max = 200, groups = AdvanceInfo.class)
	private String description;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate releaseDate;
	@DurationMin(minutes = 0, groups = AdvanceInfo.class)
	@JsonDeserialize(using = DurationDeserializer.class)
	@JsonSerialize(using = DurationSerializer.class)
	private Duration duration;
	private Set<Long> likedBy = new HashSet<>();
	private Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
	private Mpa mpa;

}
