package net.coffeemachine.service;

import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.states.ReadyState;
import net.coffeemachine.service.states.State;
import net.coffeemachine.service.states.StopState;
import net.coffeemachine.to.Info;
import net.coffeemachine.util.exception.NotEnoughSuppliesException;

@Component
@Slf4j
@ToString
public class CoffeeMachine {

    private static final int CUPS_NUM = 1;

    @ToString.Exclude
    @Autowired
    private ExecutorService coffeeMachineService;
    @ToString.Exclude
    @Autowired
    private Map<CoffeeType, Coffee> coffeeFactory;
    @ToString.Exclude
    private State state;

    @Value("${app.coffee-machine.water}")
    private int water;
    @Value("${app.coffee-machine.milk}")
    private int milk;
    @Value("${app.coffee-machine.beans}")
    private int beans;
    @Value("${app.coffee-machine.cups}")
    private int cups;

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

    // TODO - move logging to BPP or AOP
    public void makeCoffee(CoffeeType coffeeType) {
        Coffee coffee = coffeeFactory.get(coffeeType);
        checkSupplies(coffee);
        log.info("Start making coffee {}", coffee.getType());
        coffeeMachineService.submit(() -> {
            allocateSupplies(coffee);
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

    public Info remainsSupplies() {
        String info = "Remain supplies of " + this;
        log.info(info);
        return new Info(info);
    }

    public void turnOf() {
        log.info("Turn of coffee machine");
        changeState(new StopState(this));
    }

    /**
     * @deprecated (not in use anymore).
     */
    @Deprecated(since = "v2.1.0")
    private void appendSupplies(int water, int milk, int beans, int cups) {
        log.debug("Add supplies:\n - water:{}\n - milk:{}\n - beans:{}\n - cups:{}", water, milk, beans, cups);
        this.water +=water;
        this.milk +=milk;
        this.beans += beans;
        this.cups += cups;
    }

    private void allocateSupplies(Coffee coffee) {
        this.water -= coffee.getWater();
        this.milk -= coffee.getMilk();
        this.beans -= coffee.getBeans();
        this.cups -= CUPS_NUM;
    }

    private void checkSupplies(Coffee coffee) {
        StringJoiner notEnough = new StringJoiner(", ");
        if (this.water - coffee.getWater() < 0) {
            notEnough.add("water");
        }
        if (this.milk - coffee.getMilk() < 0) {
            notEnough.add("milk");
        }
        if (this.beans - coffee.getBeans() < 0) {
            notEnough.add("beans");
        }
        if (this.cups - CUPS_NUM < 0) {
            notEnough.add("cups");
        }

        if (notEnough.length() != 0) {
            throw new NotEnoughSuppliesException("Not enough " + notEnough);
        }
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
