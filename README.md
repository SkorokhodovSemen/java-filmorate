# java-filmorate
Template repository for Filmorate project.
# Data Base
![DB (2)](https://github.com/SkorokhodovSemen/java-filmorate/assets/80544964/fea48cfa-f530-43d6-94d9-075142e29bd4)
# Example
1. Запрос на получение всех фильмов:  
SELECT *  
FROM film;
2. Запрос на получение всех фильмов с расшифровкой MPA:  
SELECT *  
FROM film AS f  
LEFT JOIN mpa AS m ON m.id = f.mpa;  
3. Запрос на количество друзей у пользователя:  
SELECT r.id  
COUNT(r.id_friend)  
FROM relationship AS r  
GROUP BY r.id;  
4. Запрос на топ 10 фильмов  
SELECT f.name,  
COUNT (fl.id_user) rate  
FROM film_likes AS fl  
LEFT JOIN film AS f ON f.film_id = fl.id_film  
GROUP BY fl.id_film  
ORDER BY rate DESC  
LIMIT 10;  
