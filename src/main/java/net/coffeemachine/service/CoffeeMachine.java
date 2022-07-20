package net.coffeemachine.service;

import net.coffeemachine.model.coffee.CoffeeType;

public interface CoffeeMachine extends Machine {
    String turnOn();
    String turnOff();
    String clean();
    String make(CoffeeType type);
    String remainsSupplies();
}
