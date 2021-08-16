package net.coffeemachine.service;

import lombok.RequiredArgsConstructor;
import net.coffeemachine.model.coffee.CoffeeType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoffeeMachineCommands {
    private final Machine coffeeMachine;

    public String start() {
        return coffeeMachine.getState().onStart();
    }

    public String make(CoffeeType coffeeType) {
        return coffeeMachine.getState().onMake(coffeeType);
    }

    public String remains() {
        return coffeeMachine.getState().onRemain();
    }

    public String clean() {
        return coffeeMachine.getState().onClean();
    }

    public String stop() {
        return coffeeMachine.getState().onStop();
    }
}
