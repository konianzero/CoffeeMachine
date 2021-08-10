package net.coffeemachine.service.states;

import lombok.Setter;
import net.coffeemachine.service.Machine;
import net.coffeemachine.to.Info;
import net.coffeemachine.model.coffee.CoffeeType;

// TODO - Refactor State Pattern with Spring -> Spring State machine ?
@Setter
public abstract class State {
    protected Machine<CoffeeType> machine;

    public Info onStart()                     { return new Info(getInfo()); }
    public Info onMake(CoffeeType coffeeType) { return new Info(getInfo()); }
    public Info onRemain()                    { return new Info(getInfo()); }
    public Info onClean()                     { return new Info(getInfo()); }
    public Info onStop()                      { return new Info(getInfo()); }

    public abstract StateType getType();
    public abstract String getInfo();
}
