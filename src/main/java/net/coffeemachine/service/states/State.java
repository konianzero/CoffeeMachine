package net.coffeemachine.service.states;

import net.coffeemachine.to.Info;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.CoffeeMachine;

public abstract class State {
    CoffeeMachine machine;

    public State(CoffeeMachine machine) {
        this.machine = machine;
    }

    public Info onStart()                     { return new Info(toString()); }
    public Info onMake(CoffeeType coffeeType) { return new Info(toString()); }
    public Info onRemain()                    { return new Info(toString()); }
    public Info onClean()                     { return new Info(toString()); }
    public Info onStop()                      { return new Info(toString()); }
}
