Управление кофеваркой через браузер
-----------------------------------

Программа по управлению кофеваркой через браузер.

---

**Запуск**
```
mvn clean package org.codehaus.cargo:cargo-maven2-plugin:1.8.2:run
```

URL: [http://localhost:8080/coffeemachine/control](http://localhost:8080/coffeemachine/control)

---

## Документация API

[Swagger Api Documentation](http://localhost:8080/coffeemachine/swagger-ui.html)

| API                     | Method | Description            | URL                                   |
|-------------------------|--------|------------------------|---------------------------------------|
| CoffeeMachineController | PATCH  | Включить кофеварку     | {URL}/start                           |
|                         | PATCH  | Сделать кофе           | {URL}/make?coffeeType={coffeeType}    |
|                         | PATCH  | Остатки ингредиентов   | {URL}/remains                         |
|                         | PATCH  | Почистить кофеварку    | {URL}/clean                           |
|                         | PATCH  | Выключить кофеварку    | {URL}/stop                            |

---