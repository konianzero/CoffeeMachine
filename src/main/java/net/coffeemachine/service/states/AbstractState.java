package net.coffeemachine.service.states;

import lombok.Setter;
import net.coffeemachine.service.Machine;
import net.coffeemachine.model.coffee.CoffeeType;

/**
 * @deprecated (use states from StateMachine)
 */
@Deprecated(since = "2.1.2")
@Setter
public abstract class AbstractState implements State {
    protected Machine machine;

    @Override
    public String onStart()                     { return getInfo(); }
    @Override
    public String onMake(CoffeeType coffeeType) { return getInfo(); }
    @Override
    public String onRemain()                    { return getInfo(); }
    @Override
    public String onClean()                     { return getInfo(); }
    @Override
    public String onStop()                      { return getInfo(); }

}
