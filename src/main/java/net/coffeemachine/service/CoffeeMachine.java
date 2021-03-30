package net.coffeemachine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.coffeemachine.model.CoffeeFactory;
import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.states.ReadyState;
import net.coffeemachine.service.states.State;
import net.coffeemachine.service.states.StopState;
import net.coffeemachine.to.Info;
import net.coffeemachine.util.exception.NotEnoughSuppliesException;

@Component
public class CoffeeMachine {

    private static final int CUPS_NUM = 1;
    private static final Logger log = LoggerFactory.getLogger(CoffeeMachine.class);

    private final CoffeeFactory coffeeFactory;
    private final ExecutorService service;

    private State state;

    private int water;
    private int milk;
    private int beans;
    private int cups;

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

    public void makeCoffee(CoffeeType coffeeType) {
        Coffee coffee = coffeeFactory.createCoffee(coffeeType);
        log.info("Start making coffee {}", coffee.getName());
        service.submit(() -> {
            processing(coffee.getTimeToMake());
            log.info("Coffee {} is ready", coffee.getName());
            changeState(new ReadyState(this));
        });
    }

    public void clean() {
        log.info("Start cleaning coffee machine");
        service.submit(() -> {
            processing(60000);
            log.info("Coffee machine is clean");
            changeState(new ReadyState(this));
        });
    }

    public Info remainsSupplies() {
        String info = "Coffee machine " + suppliesToString();
        log.info(info);
        return new Info(info);
    }

    public void turnOf() {
        log.info("Turn of coffee machine");
        changeState(new StopState(this));
    }

    public CoffeeMachine(CoffeeFactory coffeeFactory) {
        state = new ReadyState(this);

        this.coffeeFactory = coffeeFactory;
        coffeeFactory.setCoffeeMachine(this);

        service = Executors.newSingleThreadExecutor();

        appendSupplies(500, 540, 120, 9);
    }

    public void appendSupplies(int water, int milk, int beans, int cups) {
        log.debug("Add supplies:\n - water:{}\n - milk:{}\n - beans:{}\n - cups:{}", water, milk, beans, cups);
        this.water +=water;
        this.milk +=milk;
        this.beans += beans;
        this.cups += cups;
    }

    public void allocateSupplies(int water, int milk, int beans) {
        if (isEnoughSupplies(water, milk, beans)) {
            this.water -= water;
            this.milk -= milk;
            this.beans -= beans;
            this.cups -= CUPS_NUM;
        }
    }

    private boolean isEnoughSupplies(int water, int milk, int beans) {
        StringJoiner notEnough = new StringJoiner(", ");
        if (this.water - water < 0) {
            notEnough.add("water");
        } else if (this.milk - milk < 0) {
            notEnough.add("milk");
        } else if (this.beans - beans < 0) {
            notEnough.add("beans");
        } else if (this.cups - CUPS_NUM < 0) {
            notEnough.add("cups");
        }

        if (notEnough.length() != 0) {
            throw new NotEnoughSuppliesException("Not enough " + notEnough);
        }
        return true;
    }

    private void processing(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String suppliesToString() {
        return "Remain supplies {" +
                "water=" + water +
                ", milk=" + milk +
                ", beans=" + beans +
                ", cups=" + cups +
                '}';
    }
}
