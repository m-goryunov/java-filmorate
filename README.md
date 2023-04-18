## Filmorate Application by MG
### Project ER Diagram:
![](db/DB_Filmorate.png)

Ссылка на диаграму с её кодом:
https://app.quickdatabasediagrams.com/#/d/A1hIWc

### Примеры запросов:
```
Get all users, order by id:
SELECT *
FROM user
ORDER BY id DESC
```

```
Get user friends count:
SELECT u.user_id, COUNT(uf.friend_id)
FROM user AS u 
LEFT JOIN user_friends AS uf ON uf.user_id = u.user_id
GROUP BY u.user_id
ORDER BY u.user_id DESC
```