package net.coffeemachine.service.states;

import org.springframework.stereotype.Component;

// TODO - Stopping machine when make coffee
@Component
public class MakeState extends State {

    @Override
    public StateType getType() {
        return StateType.MAKE;
    }

    public String getInfo() {
        return "Coffee Machine is making coffee, please wait.";
    }
}
