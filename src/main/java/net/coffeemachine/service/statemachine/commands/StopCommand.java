package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import net.coffeemachine.service.Machine;
import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;

@Component
@RequiredArgsConstructor
public class StopCommand implements Command {

    private final Machine coffeeMachine;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        coffeeMachine.turnOff();
        stateContext.getExtendedState().getVariables().put("info", "Stop coffee machine");
    }

    @Override
    public Events getType() {
        return Events.STOPPING;
    }
}
