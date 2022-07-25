package net.coffeemachine.service;

import java.util.function.Consumer;

public interface Machine {
    void turnOn();
    void turnOff();
    void startTask(int millis);
    void afterTask(Consumer<Boolean> action);
}
