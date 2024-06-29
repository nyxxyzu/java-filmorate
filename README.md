Схема базы данных приложения Filmorate

![dbscheme](https://github.com/nyxxyzu/java-filmorate/assets/157836043/ca05d620-1161-461c-a0cb-586b202f323b)

База содержит 6 таблиц:
1. Users. Таблица содержит информацию о пользователях.
2. Friends. Таблица содержит информацию о запросах добавления в друзья и их статус (принят или не принят).
3. Films. Таблица содержит информацию о фильмах.
4. Rating. Таблица содержит рейтинги, которые могут быть у фильмов.
5. Genres. Таблица содержит жанры, которые могут быть у фильмов.
6. Film_genre. Таблица содержит жанры, относящиеся к каждому фильму.

Примеры запросов к базе для операций приложения:

``` sql
SELECT *
FROM users
```

Возвращает список пользователей приложения.






