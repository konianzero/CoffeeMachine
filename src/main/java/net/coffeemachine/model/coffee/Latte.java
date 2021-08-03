package net.coffeemachine.model.coffee;

import org.springframework.stereotype.Component;

@Component
public class Latte extends Coffee {
    public Latte() {
        timeToMake = 30000;
        water = 350;
        milk = 75;
        beans = 20;
    }

    @Override
    public CoffeeType getType() {
        return CoffeeType.LATTE;
    }
}
