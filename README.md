Управление кофеваркой через браузер
-----------------------------------

Ветка проекта: `SOAP`
```shell
─── master    
    │
    ├── BPP (Логгирование действий кофемашины в базу данных через Bean Post Processor)
    │
    └── SOAP (Управление кофемашиной по SOAP)
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

Для эмуляции действий кофемашины используется [CoffeeMachineEquipment.java](src/main/java/net/coffeemachine/service/CoffeeMachineEquipment.java).
Действия кофемашины логгируются в базу данных по средствам аннотации [@DatabaseLogging](src/main/java/net/coffeemachine/util/aspect/DatabaseLogging.java),
аспекта [DatabaseLoggingAspect.java](src/main/java/net/coffeemachine/util/aspect/DatabaseLoggingAspect.java) и 
DBAppender`а ([logback-spring.xml](src/main/resources/logback-spring.xml)).  

Управление осуществляется по средствам SOAP ([wsdl](src/main/resources/wsdl/commands.wsdl) и [config](src/main/java/net/coffeemachine/config/WebServiceConfig.java)), сообщения обрабатываются 
[CoffeeMachineEndpoint.java](src/main/java/net/coffeemachine/web/controller/CoffeeMachineEndpoint.java).  

Сгенерировать код на основании wsdl:
```shell
mvn clean jaxws:wsimport
```

Маппинг классов осуществляется [MapStructMapper](src/main/java/net/coffeemachine/util/mapper/MapStructMapper.java).

Конфигурация:  
    - [application.yaml](src/main/resources/application.yaml)  
    - [net.coffeemachine.config](src/main/java/net/coffeemachine/config)  

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
```shell
mvn spring-boot:run
```

Запуск с профилем ```prod```
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

URL: [http://localhost:8080/coffeemachine/ws](http://localhost:8080/coffeemachine/ws)

[H2 console](http://localhost:8080/coffeemachine/h2-console)

---

### Документация API

| API                     | Description                                                                     | WSDL                                                           |
|-------------------------|---------------------------------------------------------------------------------|----------------------------------------------------------------|
| Coffeemachine commands  | Команды для кофеварки (вкл, выкл, сделать кофе, очистка, остатки ингредиентов)  | http://localhost:8080/coffeemachine/ws/commandsDefinition.wsdl |

_*Примеры сообщений*_:

- Включить кофемашину
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:cmd="http://coffeemachine.net/commands">
    <soapenv:Header/>
    <soapenv:Body>
        <cmd:Action>
            <cmd:actionType>Start</cmd:actionType>
        </cmd:Action>
    </soapenv:Body>
</soapenv:Envelope>
```
- Приготовить кофе латте
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:cmd="http://coffeemachine.net/commands">
    <soapenv:Header/>
    <soapenv:Body>
        <cmd:Action>
            <cmd:actionType>Make</cmd:actionType>
            <cmd:coffeeType>Latte</cmd:coffeeType>
        </cmd:Action>
    </soapenv:Body>
</soapenv:Envelope>
```
---