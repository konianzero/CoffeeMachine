package net.coffeemachine.service;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
public class CoffeeMachineImpl implements CoffeeMachine {
    @Getter
    private final Map<CoffeeType, CoffeeRecipe> recipes;
    @Getter
    private final Supplies supplies;
    private final ObjectFactory<ExecutorService> prototypeBeanObjectFactory;

    private ExecutorService equipment;
    private CompletableFuture<Boolean> runningTask;

    public ExecutorService startEquipment() {
        return prototypeBeanObjectFactory.getObject();
    }

    @Override
    public void turnOn() {
        equipment = startEquipment();
    }

    @Override
    public void turnOff() {
        shutDownCoffeeMachineEquipment();
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

    @Override
    public void startTask(int millis) {
        runningTask = CompletableFuture
                .supplyAsync(() -> processing(millis), equipment);
    }

    private void shutDownCoffeeMachineEquipment() {
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
