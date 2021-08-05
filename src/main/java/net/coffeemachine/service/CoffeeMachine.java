package net.coffeemachine.service;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.coffeemachine.model.ingredients.Supplies;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.states.ReadyState;
import net.coffeemachine.service.states.State;
import net.coffeemachine.service.states.StopState;
import net.coffeemachine.to.Info;

@Component
@DependsOn({"dataSource"})
@Slf4j
@RequiredArgsConstructor
public class CoffeeMachine {

    private final ExecutorService coffeeMachineService;
    private final Map<CoffeeType, Coffee> coffeeFactory;
    private final Supplies supplies;

    private State state;

    @PostConstruct
    public void init() {
        turnOn();
    }

    public State getState() {
        return state;
    }

    public void changeState(State state) {
        this.state = state;
    }

    public void turnOn() {
        log.info("Turn on coffee machine");
        changeState(new ReadyState(this));
    }

    // TODO - Move logging to BPP or AOP
    public void makeCoffee(CoffeeType coffeeType) {
        Coffee coffee = coffeeFactory.get(coffeeType);
        supplies.check(coffee);
        log.info("Start making coffee {}", coffee.getType());
        coffeeMachineService.submit(() -> {
            supplies.allocate(coffee);
            processing(coffee.getTimeToMake());
            log.info("Coffee {} is ready", coffee.getType());
            changeState(new ReadyState(this));
        });
    }

    public void clean() {
        log.info("Start cleaning coffee machine");
        coffeeMachineService.submit(() -> {
            processing(60000);
            log.info("Coffee machine is clean");
            changeState(new ReadyState(this));
        });
    }

    // TODO - Show remains when clean or make coffee
    public Info remainsSupplies() {
        String remains = supplies.toString();
        log.info(remains);
        return new Info(remains);
    }

    // TODO - Stop coffeeMachineService when turn of machine
    public void turnOf() {
        log.info("Turn of coffee machine");
        changeState(new StopState(this));
    }

    // TODO - Add throws Exception to method to create more informative log
    private void processing(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            log.warn("Stop processing!", ie);
            Thread.currentThread().interrupt();
        }
    }
}
