package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import net.coffeemachine.service.Machine;
import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;

@Component
@RequiredArgsConstructor
public class StopCommand implements Command {

    private final Machine coffeeMachine;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        coffeeMachine.turnOff();
    }

    @Override
    public Events getType() {
        return Events.STOPPING;
    }
}
