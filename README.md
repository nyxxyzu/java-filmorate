Filmorate - сервис позволяющий пользователям оценивать и искать фильмы, в соотвествии с их оценками.

Использованные технологии: Java, Spring Boot, Maven, H2, JDBC, JUnit.

Схема базы данных приложения Filmorate

![dbscheme1](https://github.com/nyxxyzu/java-filmorate/assets/157836043/b574c162-6e57-4b4a-882f-66b71156b8c9)

База содержит 7 таблиц:
1. Users. Таблица содержит информацию о пользователях.
2. Friends. Таблица содержит информацию о запросах добавления в друзья и их статус (принят или не принят).
3. Films. Таблица содержит информацию о фильмах.
4. Rating. Таблица содержит рейтинги, которые могут быть у фильмов.
5. Genres. Таблица содержит жанры, которые могут быть у фильмов.
6. Film_genre. Таблица содержит жанры, относящиеся к каждому фильму.
7. Likes. Таблица содержит информацию о лайках пользователей.

Примеры запросов к базе для операций приложения:

``` sql
SELECT *
FROM users
```

Возвращает список пользователей приложения.

``` sql
SELECT *
FROM users WHERE id = ?
```

Возвращает пользователя по его id.

``` sql
INSERT INTO users(email, login, name, birthday)
VALUES (?, ?, ?, ?) returning id
```

Добавляет в таблицу нового пользователя.






