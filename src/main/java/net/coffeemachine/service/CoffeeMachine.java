package net.coffeemachine.service;

import java.util.Map;
import java.util.concurrent.*;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.coffeemachine.util.aspect.DatabaseLogging;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import net.coffeemachine.model.Supplies;
import net.coffeemachine.service.states.*;
import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.model.coffee.CoffeeType;

import static net.coffeemachine.service.states.StateType.*;

// TODO - Move logging to BPP or AOP
@Component
@DependsOn({"dataSource"})
@Slf4j
@RequiredArgsConstructor
public class CoffeeMachine implements Machine {

    private final Map<CoffeeType, Coffee> coffeeFactory;
    private final Map<StateType, State> states;
    private final Supplies supplies;

    private ExecutorService coffeeMachineService;
    private State state;
    private CompletableFuture<Void> runningTask;

    // TODO - @DatabaseLogging NOT WORK -> set Machine in init()?
    @PostConstruct
    public void init() {
        states.forEach((k, v) -> v.setMachine(this));
        turnOn();
    }

    @Lookup
    public ExecutorService getCoffeeMachineService() {
        return null;
    }

    @Override
    public State getState() {
        return state;
    }

    private void changeStateTo(StateType stateType) {
        this.state = states.get(stateType);
    }

    @Override
    @DatabaseLogging
    public String turnOn() {
        changeStateTo(READY);
        coffeeMachineService = getCoffeeMachineService();
        log.info("Turn on coffee machine");
        return "Turn on coffee machine";
    }

    @Override
    @DatabaseLogging
    public String make(CoffeeType coffeeType) {
        changeStateTo(MAKE);
        Coffee coffee = coffeeFactory.get(coffeeType);
        if (!supplies.isEnoughFor(coffee)) {
            log.info("Not enough ingredients for {}: {}", coffee.getType(), supplies.getNotEnough());
            changeStateTo(READY);
            return "Not enough ingredients";
        }

        log.info("Start making coffee {}", coffee.getType());
        supplies.allocate(coffee);
        startTask(coffee.getTimeToMake(), String.format("Coffee %s is ready", coffee.getType()));
        return "Start making coffee";
    }

    @Override
    @DatabaseLogging
    public String clean() {
        changeStateTo(CLEAN);
        log.info("Start cleaning coffee machine");
        startTask(60000, "Coffee machine is clean");
        return "Start cleaning coffee machine";
    }

    @Override
    @DatabaseLogging
    public String remainsSupplies() {
        String remains = supplies.toString();
        log.info(remains);
        return remains;
    }

    @Override
    @DatabaseLogging
    public String turnOff() {
        changeStateTo(STOP);
        log.info("Turn of coffee machine");
        shutDownCoffeeMachineService();
        return "Turn of coffee machine";
    }

    private boolean processing(int millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException ie) {
            log.warn("Stop processing!", ie);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    private void startTask(int millis, String msg) {
        runningTask = CompletableFuture
                .supplyAsync(() -> processing(millis), coffeeMachineService)
                .thenAccept(result -> {
                    if (result) {
                        log.info(msg);
                        changeStateTo(READY);
                    }
                });
    }

    private void shutDownCoffeeMachineService() {
        if (runningTask != null) runningTask.cancel(true);
        coffeeMachineService.shutdown();
        try {
            if (!coffeeMachineService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                coffeeMachineService.shutdownNow();
            }
        } catch (InterruptedException ie) {
            log.warn("Stop coffee machine service!", ie);
            coffeeMachineService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
