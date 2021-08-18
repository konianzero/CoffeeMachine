package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import net.coffeemachine.service.Machine;
import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;

@Component
@RequiredArgsConstructor
public class RemainsCommand implements Command {

    private final Machine coffeeMachine;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        stateContext.getExtendedState().getVariables().put("supplies", coffeeMachine.remainsSupplies());
    }

    @Override
    public Events getType() {
        return Events.REMAINING;
    }
}
