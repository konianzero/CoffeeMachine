package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import net.coffeemachine.service.CoffeeMachine;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;
import net.coffeemachine.model.coffee.CoffeeType;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MakeCommand implements Command {

    private final CoffeeMachine coffeeMachine;

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
