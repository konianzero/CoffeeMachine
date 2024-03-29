Управление кофеваркой через браузер
-----------------------------------

Ветка проекта: `master`
```shell
─── master (REST)
    │
    └── SOAP
```
___

Программа по управлению кофеваркой через браузер.

В программе используется [Spring Statemachine](https://docs.spring.io/spring-statemachine/docs/3.0.1/reference/) для 
моделирования состояний кофемашины и переходов между ними 
([StateMachineConfig.java](src/main/java/net/coffeemachine/config/StateMachineConfig.java)).
При переходах в новое состояние выполняются действия 
([паттерн 'Команда'](src/main/java/net/coffeemachine/service/statemachine/commands)).  

Кофемашина делает [3 вида кофе](src/main/java/net/coffeemachine/model/coffee/CoffeeType.java), кол-во ингредиентов и 
время приготовления для каждого вида находится в [consumables.yaml](src/main/resources/consumables.yaml), эти параметры 
внедряются в соответствующие классы по средствам `@Value`, например в [Cappuccino.java](src/main/java/net/coffeemachine/model/coffee/Cappuccino.java).

Для эмуляции действий кофемашины используется [CoffeeMachineImpl.java](src/main/java/net/coffeemachine/service/CoffeeMachineImpl.java).
Действия кофемашины логгируются в базу данных по средствам аннотации [@LogToDB](src/main/java/net/coffeemachine/util/aspect/LogToDB.java),
аспекта [DatabaseLoggingAspect.java](src/main/java/net/coffeemachine/util/aspect/DatabaseLoggingAspect.java) и 
DBAppender`а ([logback-spring.xml](src/main/resources/logback-spring.xml)).  

Управление осуществляется по средствам HTTP запросов которые обрабатываются 
[CoffeeMachineController.java](src/main/java/net/coffeemachine/web/controller/CoffeeMachineController.java).  

Конфигурация:  
    - [application.yaml](src/main/resources/application.yaml)  
    - [net.coffeemachine.config](src/main/java/net/coffeemachine/config)  

---

### Требования

- JDK 17
- maven 3

---

### Запуск

* Профили:
    
  - ```dev``` По умолчанию, база данных H2 (In memory)
  - ```prod``` База данных PostgreSQL

* Запуск
    ```
    mvn spring-boot:run
    ```

* Запуск с профилем ```prod```
    ```
    mvn spring-boot:run -Dspring-boot.run.profiles=prod
    ```

* Запустить с maven wrapper и указанным JDK: [run.sh](run.sh)

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