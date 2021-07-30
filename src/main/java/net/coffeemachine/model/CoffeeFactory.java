package net.coffeemachine.model;

import net.coffeemachine.model.coffee.*;
import org.springframework.stereotype.Component;

@Component
public class CoffeeFactory {

    public Coffee getCoffee(CoffeeType type) {
        return switch (type) {
            case ESPRESSO -> new Espresso();
            case CAPPUCCINO -> new Cappuccino();
            case LATTE -> new Latte();
        };
    }
}
