package net.coffeemachine.model.coffee;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class Espresso extends Coffee {

    public Espresso(@Value("${espresso.time-to-make}") int timeToMake,
                    @Value("${espresso.water}") int water,
                    @Value("${espresso.beans}") int beans) {
        this.timeToMake = timeToMake;
        this.water = water;
        this.beans = beans;
    }

    @Override
    public CoffeeType getType() {
        return CoffeeType.ESPRESSO;
    }
}
