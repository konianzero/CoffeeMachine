package net.coffeemachine.service;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.coffeemachine.util.aspect.LogToDB;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import net.coffeemachine.model.Supplies;
import net.coffeemachine.model.coffee.CoffeeRecipe;
import net.coffeemachine.model.coffee.CoffeeType;

@Component
@DependsOn({"dataSource"})
@Slf4j
@RequiredArgsConstructor
public class CoffeeMachineEquipment implements CoffeeMachine {
    private final Map<CoffeeType, CoffeeRecipe> coffeeFactory;
    private final Supplies supplies;
    private final ObjectFactory<ExecutorService> prototypeBeanObjectFactory;

    private ExecutorService equipment;
    private CompletableFuture<Boolean> runningTask;

    public ExecutorService startEquipment() {
        return prototypeBeanObjectFactory.getObject();
    }

    @Override
    @LogToDB
    public String turnOn() {
        equipment = startEquipment();
        return "Turn on equipment";
    }

    @Override
    @LogToDB
    public String make(CoffeeType coffeeType) {
        CoffeeRecipe coffeeRecipe = coffeeFactory.get(coffeeType);
        if (!supplies.isEnoughFor(coffeeRecipe)) {
            return String.format("Not enough ingredients for %s: %s", coffeeRecipe.getType(), supplies.getNotEnough());
        }

        supplies.allocate(coffeeRecipe);
        startTask(coffeeRecipe.timeToMake());
        return String.format("Start making coffee %s", coffeeRecipe.getType());
    }

    @Override
    @LogToDB
    public String clean() {
        startTask(6000);
        return "Start cleaning coffee machine";
    }

    @Override
    @LogToDB
    public String remainsSupplies() {
        return supplies.toString();
    }

    @Override
    @LogToDB
    public String turnOff() {
        shutDownCoffeeMachineService();
        return "Turn of equipment";
    }

    @Override
    public void afterTask(Consumer<Boolean> action) {
        runningTask.thenAccept(action);
    }

    private boolean processing(int millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        return false;
    }

    private void startTask(int millis) {
        runningTask = CompletableFuture
                .supplyAsync(() -> processing(millis), equipment);
    }

    private void shutDownCoffeeMachineService() {
        // Disable new tasks from being submitted
        equipment.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!equipment.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                // Cancel currently executing tasks
                equipment.shutdownNow();
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            equipment.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        } finally {
            equipment = null;
        }
    }
}
