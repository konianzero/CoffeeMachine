package net.coffeemachine.service.states;

import org.springframework.stereotype.Component;

@Component
public class StopState extends State {

    @Override
    public String onStart() {
        return machine.turnOn();
    }

    @Override
    public StateType getType() {
        return StateType.STOP;
    }

    public String getInfo() {
        return "Coffee Machine is stopped";
    }
}
