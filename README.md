[Ссылка на Pull Request, ДЗ#1](https://github.com/nikguscode/YLab/pull/1)

[Ссылка на Pull Request, ДЗ#2](https://github.com/nikguscode/YLab/pull/2)

Стек:
1) Java 17
2) JUnit 5
3) AssertJ
4) Mockito
5) PostgreSQL
6) Liquibase
7) Lombok

Запуск кода:
1) docker-compose -f src/main/resources/db/docker-compose.yml up -d 
2) mvn compile
3) mvn package
4) java -jar target\YLabProject-1.0-SNAPSHOT.jar
