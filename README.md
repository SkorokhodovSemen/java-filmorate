# java-filmorate
Template repository for Filmorate project.
# Data Base
![MyDB](https://github.com/SkorokhodovSemen/java-filmorate/assets/80544964/c1e72e4b-19a2-4a58-817f-f3a15253285c)
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
