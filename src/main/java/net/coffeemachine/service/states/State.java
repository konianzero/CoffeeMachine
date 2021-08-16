package net.coffeemachine.service.states;

import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.Machine;

/**
 * @deprecated (use states from StateMachine)
 */
@Deprecated(since = "2.1.2")
public interface State {
    String onStart();

    String onMake(CoffeeType coffeeType);

    String onRemain();

    String onClean();

    String onStop();

    StateType getType();

    String getInfo();

    void setMachine(Machine machine);
}
