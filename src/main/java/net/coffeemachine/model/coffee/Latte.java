package net.coffeemachine.model.coffee;

import net.coffeemachine.service.CoffeeMachine;

public class Latte extends Coffee {
    public Latte(CoffeeMachine coffeeMachine) {
        super(coffeeMachine);
        name = CoffeeType.LATTE.name();
        timeToMake = 30000;
        water = 350;
        milk = 75;
        beans = 20;
    }
}
