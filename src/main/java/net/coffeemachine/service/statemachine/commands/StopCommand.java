package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import net.coffeemachine.service.CoffeeMachine;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;

@Component
@RequiredArgsConstructor
public class StopCommand extends Command {

    private final CoffeeMachine coffeeMachine;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        coffeeMachine.turnOff();
        stateContext.getExtendedState().getVariables().put("info", "Turn of equipment");
    }

    @Override
    public Events getType() {
        return Events.STOPPING;
    }
}
