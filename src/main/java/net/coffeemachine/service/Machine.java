package net.coffeemachine.service;

import java.util.function.Consumer;

public interface Machine {
    void afterTask(Consumer<Boolean> action);
}
