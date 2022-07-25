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
public class StartCommand extends Command {

    private final CoffeeMachine coffeeMachine;

    @Override
    @LogToDB
    public void execute(StateContext<States, Events> stateContext) {
        coffeeMachine.turnOn();
        stateContext.getExtendedState().getVariables().put("info", "Turn on equipment");
    }

    @Override
    public Events getType() {
        return Events.STARTING;
    }
}
