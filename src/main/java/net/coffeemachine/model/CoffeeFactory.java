package net.coffeemachine.model;

import net.coffeemachine.model.coffee.*;
import net.coffeemachine.service.CoffeeMachine;
import org.springframework.stereotype.Component;

@Component
public class CoffeeFactory {

    private CoffeeMachine coffeeMachine;

    public void setCoffeeMachine(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    public Coffee createCoffee (CoffeeType type) {
        Coffee coffee = null;

        switch (type) {
            case ESPRESSO:
                coffee = new Espresso(coffeeMachine);
                break;
            case CAPPUCCINO:
                coffee = new Cappuccino(coffeeMachine);
                break;
            case LATTE:
                coffee = new Latte(coffeeMachine);
                break;
        }

        coffee.prepare();

        return coffee;
    }
}
