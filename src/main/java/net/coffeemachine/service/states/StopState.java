package net.coffeemachine.service.states;

import net.coffeemachine.to.Info;
import org.springframework.stereotype.Component;

@Component
public class StopState extends State {

    @Override
    public Info onStart() {
        return new Info(machine.turnOn());
    }

    @Override
    public StateType getType() {
        return StateType.STOP;
    }

    public String getInfo() {
        return "Coffee Machine is stopped";
    }
}
