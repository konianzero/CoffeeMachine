package net.coffeemachine.service.states;

import org.springframework.stereotype.Component;

import net.coffeemachine.to.Info;

@Component
public class CleanState extends State {

    @Override
    public Info onStop() {
        return new Info(machine.turnOff());
    }

    @Override
    public Info onRemain() {
        return new Info(machine.remainsSupplies());
    }

    @Override
    public StateType getType() {
        return StateType.CLEAN;
    }

    public String getInfo() {
        return "Coffee Machine is cleaning, please wait.";
    }
}
