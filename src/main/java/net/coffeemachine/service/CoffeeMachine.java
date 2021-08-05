package net.coffeemachine.service;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.coffeemachine.model.ingredients.Supplies;
import net.coffeemachine.service.states.*;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.model.coffee.CoffeeType;

import static net.coffeemachine.service.states.StateType.*;

@Component
@DependsOn({"dataSource"})
@Slf4j
@RequiredArgsConstructor
public class CoffeeMachine {

    private final ExecutorService coffeeMachineService;
    private final Map<CoffeeType, Coffee> coffeeFactory;
    private final Map<StateType, State> states;
    private final Supplies supplies;

    private State state;

    @PostConstruct
    public void init() {
        states.forEach((k, v) -> v.setMachine(this));
        turnOn();
    }

    public State getState() {
        return state;
    }

    public void changeStateTo(StateType stateType) {
        this.state = states.get(stateType);
    }

    public String turnOn() {
        log.info("Turn on coffee machine");
        changeStateTo(READY);
        return "Turn on coffee machine";
    }

    // TODO - Move logging to BPP or AOP
    public String makeCoffee(CoffeeType coffeeType) {
        changeStateTo(MAKE);
        Coffee coffee = coffeeFactory.get(coffeeType);
        if (!supplies.isEnoughFor(coffee)) {
            log.info("Not enough ingredients for {}: {}", coffee.getType(), supplies.getNotEnough());
            changeStateTo(READY);
            return "Not enough ingredients";
        }

        log.info("Start making coffee {}", coffee.getType());
        coffeeMachineService.submit(() -> {
            supplies.allocate(coffee);
            processing(coffee.getTimeToMake());
            log.info("Coffee {} is ready", coffee.getType());
            changeStateTo(READY);
        });
        return "Start making coffee";
    }

    public String clean() {
        changeStateTo(CLEAN);
        log.info("Start cleaning coffee machine");
        coffeeMachineService.submit(() -> {
            processing(60000);
            log.info("Coffee machine is clean");
            changeStateTo(READY);
        });
        return "Start cleaning coffee machine";
    }

    // TODO - Show remains when clean or make coffee
    public String remainsSupplies() {
        String remains = supplies.toString();
        log.info(remains);
        return remains;
    }

    // TODO - Stop coffeeMachineService when turn of machine
    public String turnOf() {
        log.info("Turn of coffee machine");
        changeStateTo(STOP);
        return "Turn of coffee machine";
    }

    // TODO - Make more informative log
    private void processing(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            log.warn("Stop processing!", ie);
            Thread.currentThread().interrupt();
        }
    }
}
