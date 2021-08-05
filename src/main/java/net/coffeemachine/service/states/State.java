package net.coffeemachine.service.states;

import net.coffeemachine.to.Info;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.CoffeeMachine;

// TODO - Refactor State Pattern with Spring -> Spring State machine ?
public abstract class State {
    protected CoffeeMachine machine;

    public void setMachine(CoffeeMachine machine) {
        this.machine = machine;
    }

    public Info onStart()                     { return new Info(getInfo()); }
    public Info onMake(CoffeeType coffeeType) { return new Info(getInfo()); }
    public Info onRemain()                    { return new Info(getInfo()); }
    public Info onClean()                     { return new Info(getInfo()); }
    public Info onStop()                      { return new Info(getInfo()); }

    public abstract StateType getType();
    public abstract String getInfo();
}
