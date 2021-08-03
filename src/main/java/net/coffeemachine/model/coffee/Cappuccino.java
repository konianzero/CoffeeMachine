package net.coffeemachine.model.coffee;

import org.springframework.stereotype.Component;

@Component
public class Cappuccino extends Coffee {
    public Cappuccino() {
        timeToMake = 45000;
        water = 200;
        milk = 100;
        beans = 12;
    }

    @Override
    public CoffeeType getType() {
        return CoffeeType.CAPPUCCINO;
    }
}
