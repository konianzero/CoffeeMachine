package net.coffeemachine.service.states;

import org.springframework.stereotype.Component;

// TODO - Stopping machine when clean
@Component
public class CleanState extends State {

    @Override
    public StateType getType() {
        return StateType.CLEAN;
    }

    public String getInfo() {
        return "Coffee Machine is cleaning, please wait.";
    }
}
