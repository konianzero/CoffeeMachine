package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.CoffeeMachineEquipment;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import net.coffeemachine.service.Machine;
import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MakeCommand implements Command {

    private final Machine coffeeMachine;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        coffeeMachine.make((CoffeeType) stateContext.getMessageHeader("coffee_type"));
        ((CoffeeMachineEquipment) coffeeMachine).getRunningTask()
                .thenAccept(result -> {
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
