package net.coffeemachine.service.states;

import org.springframework.stereotype.Component;

import net.coffeemachine.to.Info;

@Component
public class MakeState extends State {

    @Override
    public Info onStop() {
        return new Info(machine.turnOf());
    }

    @Override
    public Info onRemain() {
        return new Info(machine.remainsSupplies());
    }

    @Override
    public StateType getType() {
        return StateType.MAKE;
    }

    public String getInfo() {
        return "Coffee Machine is making coffee, please wait.";
    }
}
