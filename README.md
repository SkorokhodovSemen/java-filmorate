# Filmorate
## About  
Users of this application can rate the films they watch, add friends, search for films by director, rating and other parameters. And also receive recommendations for viewing based on your ratings.  
# Data Base
![DB (2)](https://github.com/SkorokhodovSemen/java-filmorate/assets/80544964/fea48cfa-f530-43d6-94d9-075142e29bd4)
# How to use  
Download application and run FilmorateApplication.  
Use settings Java-11.  
# Example code
1. Запрос на количество друзей у пользователя:  
SELECT r.id  
COUNT(r.id_friend)  
FROM relationship AS r  
GROUP BY r.id;  
2. Запрос на топ 10 фильмов  
SELECT f.name,  
COUNT (fl.id_user) rate  
FROM film_likes AS fl  
LEFT JOIN film AS f ON f.film_id = fl.id_film  
GROUP BY fl.id_film  
ORDER BY rate DESC  
LIMIT 10;  
##  Technology stack  
* SpringBoot  
* JDBC  
* PostgreSQL  
* Apache Maven
* A Collaborative Filtering  
