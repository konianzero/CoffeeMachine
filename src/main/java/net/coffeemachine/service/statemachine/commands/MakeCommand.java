package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import net.coffeemachine.service.Machine;
import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;
import net.coffeemachine.model.coffee.CoffeeType;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MakeCommand implements Command {

    private final Machine coffeeMachine;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        String info = coffeeMachine.make((CoffeeType) stateContext.getMessageHeader("coffee_type"));
        stateContext.getExtendedState().getVariables().put("info", info);
        coffeeMachine.afterTask(result -> {
            if (result) {
                stateContext.getStateMachine()
                        .sendEvent(Mono.just(MessageBuilder
                                .withPayload(Events.DONE).build()))
                        .subscribe();
            }
        });
    }

    @Override
    public Events getType() {
        return Events.MAKING;
    }
}
