package net.coffeemachine.service.states;

import lombok.Setter;
import net.coffeemachine.service.Machine;
import net.coffeemachine.model.coffee.CoffeeType;

// TODO - Refactor State Pattern with Spring -> Spring State machine ?
@Setter
public abstract class State {
    protected Machine machine;

    public String onStart()                     { return getInfo(); }
    public String onMake(CoffeeType coffeeType) { return getInfo(); }
    public String onRemain()                    { return getInfo(); }
    public String onClean()                     { return getInfo(); }
    public String onStop()                      { return getInfo(); }

    public abstract StateType getType();
    public abstract String getInfo();
}
