package net.coffeemachine.model.coffee;

import net.coffeemachine.service.CoffeeMachine;

public class Espresso extends Coffee {
    public Espresso(CoffeeMachine coffeeMachine) {
        super(coffeeMachine);
        name = CoffeeType.ESPRESSO.name().toLowerCase();
        timeToMake = 20000;
        water = 250;
        beans = 16;
    }
}
