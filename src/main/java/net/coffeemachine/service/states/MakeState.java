package net.coffeemachine.service.states;

import net.coffeemachine.service.CoffeeMachine;

public class MakeState extends State {

    public MakeState(CoffeeMachine machine) {
        super(machine);
    }

    @Override
    public String toString() {
        return "Coffee Machine is making coffee, please wait.";
    }
}
