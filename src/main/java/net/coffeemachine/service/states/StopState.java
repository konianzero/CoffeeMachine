package net.coffeemachine.service.states;

import net.coffeemachine.to.Info;
import net.coffeemachine.service.CoffeeMachine;

public class StopState extends State {

    public StopState(CoffeeMachine machine) {
        super(machine);
    }

    @Override
    public Info onStart() {
        machine.turnOn();
        machine.changeState(new ReadyState(machine));
        return new Info("Coffee Machine is starting");
    }

    @Override
    public String toString() {
        return "Coffee Machine is stopped";
    }
}
