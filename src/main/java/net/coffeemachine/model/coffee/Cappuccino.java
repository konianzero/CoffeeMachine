package net.coffeemachine.model.coffee;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class Cappuccino extends Coffee {

    public Cappuccino(@Value("${cappuccino.time-to-make}") int timeToMake,
                      @Value("${cappuccino.water}") int water,
                      @Value("${cappuccino.milk}") int milk,
                      @Value("${cappuccino.beans}") int beans) {
        this.timeToMake = timeToMake;
        this.water = water;
        this.milk = milk;
        this.beans = beans;
    }

    @Override
    public CoffeeType getType() {
        return CoffeeType.CAPPUCCINO;
    }
}
