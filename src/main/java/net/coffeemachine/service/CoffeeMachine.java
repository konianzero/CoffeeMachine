package net.coffeemachine.service;

import java.util.Map;
import java.util.concurrent.*;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import net.coffeemachine.model.Supplies;
import net.coffeemachine.service.states.*;
import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.model.coffee.CoffeeType;

// TODO - Move logging to BPP or AOP
// TODO - Autowire StateMachine
@Component
@DependsOn({"dataSource"})
@Slf4j
@RequiredArgsConstructor
public class CoffeeMachine implements Machine {

    private final Map<CoffeeType, Coffee> coffeeFactory;
    private final Supplies supplies;

    private ExecutorService coffeeMachineService;
    @Getter
    private CompletableFuture<Boolean> runningTask;

    @PostConstruct
    public void init() {
        turnOn();
    }

    @Lookup
    public ExecutorService getCoffeeMachineService() {
        return null;
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public String turnOn() {
        coffeeMachineService = getCoffeeMachineService();
        log.info("Turn on coffee machine");
        return "Turn on coffee machine";
    }

    @Override
    public String make(CoffeeType coffeeType) {
        Coffee coffee = coffeeFactory.get(coffeeType);
        if (!supplies.isEnoughFor(coffee)) {
            log.info("Not enough ingredients for {}: {}", coffee.getType(), supplies.getNotEnough());
            return "Not enough ingredients";
        }

        log.info("Start making coffee {}", coffee.getType());
        supplies.allocate(coffee);
        startTask(coffee.getTimeToMake());
        return "Start making coffee";
    }

    @Override
    public String clean() {
        log.info("Start cleaning coffee machine");
        startTask(60000);
        return "Start cleaning coffee machine";
    }

    @Override
    public String remainsSupplies() {
        String remains = supplies.toString();
        log.info(remains);
        return remains;
    }

    @Override
    public String turnOff() {
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

    private void startTask(int millis) {
        runningTask = CompletableFuture
                .supplyAsync(() -> processing(millis), coffeeMachineService);
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
