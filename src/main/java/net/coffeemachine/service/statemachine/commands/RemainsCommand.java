package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import net.coffeemachine.service.CoffeeMachine;
import net.coffeemachine.util.aspect.LogToDB;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;

@Component
@RequiredArgsConstructor
public class RemainsCommand extends Command {

    private final CoffeeMachine coffeeMachine;

    @Override
    @LogToDB
    public void execute(StateContext<States, Events> stateContext) {
        stateContext.getExtendedState().getVariables().put("info", coffeeMachine.supplies().toString());
    }

    @Override
    public Events getType() {
        return Events.REMAINING;
    }
}
