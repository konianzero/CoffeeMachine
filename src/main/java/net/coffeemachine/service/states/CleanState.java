package net.coffeemachine.service.states;

import net.coffeemachine.service.CoffeeMachine;

public class CleanState extends State {

    public CleanState(CoffeeMachine machine) {
        super(machine);
    }

    @Override
    public String toString() {
        return "Coffee Machine is cleaning, please wait.";
    }
}
