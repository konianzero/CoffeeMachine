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
import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.model.coffee.CoffeeType;

// TODO - Move logging to BPP or AOP
@Component
@DependsOn({"dataSource"})
@Slf4j
@RequiredArgsConstructor
public class CoffeeMachineEquipment implements Machine {
    private final Map<CoffeeType, Coffee> coffeeFactory;
    private final Supplies supplies;

    private ExecutorService equipment;
    @Getter
    private CompletableFuture<Boolean> runningTask;

    @PostConstruct
    public void init() {
        turnOn();
    }

    @Lookup
    public ExecutorService startEquipment() {
        return null;
    }

    @Override
    public String turnOn() {
        equipment = startEquipment();
        log.info("Turn on equipment");
        return "Turn on equipment";
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
        startTask(6000);
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
        log.info("Turn of equipment");
        shutDownCoffeeMachineService();
        return "Turn of equipment";
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
                .supplyAsync(() -> processing(millis), equipment);
    }

    private void shutDownCoffeeMachineService() {
        if (runningTask != null) runningTask.cancel(true);
        equipment.shutdown();
        try {
            if (!equipment.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                equipment.shutdownNow();
            }
        } catch (InterruptedException ie) {
            log.warn("Stop coffee machine service!", ie);
            equipment.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
