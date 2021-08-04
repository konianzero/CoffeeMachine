Управление кофеваркой через браузер
-----------------------------------

_**Spring Boot**_

Программа по управлению кофеваркой через браузер.

---

### Требования
- JDK 16
- maven 3

---

### Запуск

Профили:
    
- ```dev``` По умолчанию, база данных H2 (In memory)
- ```prod``` База данных PostgreSQL

Запуск
```
mvn spring-boot:run
```

Запуск с профилем ```prod```
```
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

URL: [http://localhost:8080/coffeemachine/control](http://localhost:8080/coffeemachine/control)

[H2 console](http://localhost:8080/coffeemachine/h2-console)

---

### Документация API

[Swagger Api Documentation](http://localhost:8080/coffeemachine/swagger-ui.html)

| API                     | Method | Description            | URL                                   |
|-------------------------|--------|------------------------|---------------------------------------|
| CoffeeMachineController | PATCH  | Включить кофеварку     | {URL}/start                           |
|                         | PATCH  | Сделать кофе           | {URL}/make?coffeeType={coffeeType}    |
|                         | PATCH  | Остатки ингредиентов   | {URL}/remains                         |
|                         | PATCH  | Почистить кофеварку    | {URL}/clean                           |
|                         | PATCH  | Выключить кофеварку    | {URL}/stop                            |

---