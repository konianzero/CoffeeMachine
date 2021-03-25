package net.coffeemachine.model.coffee;

import net.coffeemachine.service.CoffeeMachine;

public class Cappuccino extends Coffee {
    public Cappuccino(CoffeeMachine coffeeMachine) {
        super(coffeeMachine);
        name = CoffeeType.CAPPUCCINO.name();
        timeToMake = 45000;
        water = 200;
        milk = 100;
        beans = 12;
    }
}
