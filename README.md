# Инфо
Обзорный проект, чтобы на практике использовать следующие технологии:
- Spring Boot, Data, MVC, AOP, Security
- Liquibase, Testcontainers и т.д.
# Как запустить
1. Устанавливаем в докере `postgres:16-alpine` с username `admin`, password `admin`, port `5432`
2. Запускаем приложение через [`ApplicationRunner`](src/main/java/com/kornienko/spring/ApplicationRunner.java), чтобы выполнились liquibase скрипты
3. В бд выполняем скрипт [`data.sql`](src/test/resources/sql/data.sql)
4. Снова запускаем приложение и смотрим доку по ссылке http://localhost:8080/swagger-ui/index.html
