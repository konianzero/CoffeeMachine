package net.coffeemachine.service;

import net.coffeemachine.service.states.State;

public interface Machine<T> {
    State getState();
    String turnOn();
    String turnOff();
    String clean();
    String make(T type);
    String remainsSupplies();
}
