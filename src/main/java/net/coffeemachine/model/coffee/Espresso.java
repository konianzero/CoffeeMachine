package net.coffeemachine.model.coffee;

import org.springframework.stereotype.Component;

@Component
public class Espresso extends Coffee {
    public Espresso() {
        timeToMake = 20000;
        water = 250;
        beans = 16;
    }

    @Override
    public CoffeeType getType() {
        return CoffeeType.ESPRESSO;
    }
}
