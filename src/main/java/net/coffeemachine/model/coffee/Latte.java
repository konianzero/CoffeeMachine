package net.coffeemachine.model.coffee;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class Latte extends CoffeeRecipe {
    public Latte(@Value("${latte.time-to-make}") int timeToMake,
                 @Value("${latte.water}") int water,
                 @Value("${latte.milk}") int milk,
                 @Value("${latte.beans}") int beans) {
        this.timeToMake = timeToMake;
        this.water = water;
        this.milk = milk;
        this.beans = beans;
    }

    @Override
    public CoffeeType getType() {
        return CoffeeType.LATTE;
    }
}
