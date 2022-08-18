package net.coffeemachine.service.statemachine.commands;

import net.coffeemachine.service.CoffeeMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;
import reactor.core.publisher.Mono;

public abstract class Command implements Action<States, Events> {
    @Autowired
    protected CoffeeMachine coffeeMachine;

    public abstract Events getType();

    protected void doneIfSuccess(boolean result, StateContext<States, Events> stateContext) {
        if (result) {
            stateContext.getStateMachine()
                    .sendEvent(Mono.just(MessageBuilder
                            .withPayload(Events.DONE).build()))
                    .subscribe();
        }
    }
}
