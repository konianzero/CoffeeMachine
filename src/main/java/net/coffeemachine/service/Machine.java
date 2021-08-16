package net.coffeemachine.service;

import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.states.State;

public interface Machine {
    /**
     * @deprecated (use states from StateMachine)
     */
    @Deprecated(since = "2.1.2")
    State getState();
    String turnOn();
    String turnOff();
    String clean();
    String make(CoffeeType type);
    String remainsSupplies();
}
