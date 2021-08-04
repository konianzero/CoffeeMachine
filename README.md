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
```
mvn spring-boot:run
```

URL: [http://localhost:8080/coffeemachine/control](http://localhost:8080/coffeemachine/control)

---

### Документация API

[Swagger Api Documentation](http://localhost:8080/swagger-ui.html)

| API                     | Method | Description            | URL                                   |
|-------------------------|--------|------------------------|---------------------------------------|
| CoffeeMachineController | PATCH  | Включить кофеварку     | {URL}/start                           |
|                         | PATCH  | Сделать кофе           | {URL}/make?coffeeType={coffeeType}    |
|                         | PATCH  | Остатки ингредиентов   | {URL}/remains                         |
|                         | PATCH  | Почистить кофеварку    | {URL}/clean                           |
|                         | PATCH  | Выключить кофеварку    | {URL}/stop                            |

---