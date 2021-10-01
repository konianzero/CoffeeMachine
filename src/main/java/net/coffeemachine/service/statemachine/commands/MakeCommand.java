package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import net.coffeemachine.service.Machine;
import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.CoffeeMachineEquipment;

@Component
@RequiredArgsConstructor
public class MakeCommand implements Command {

    private final Machine coffeeMachine;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        coffeeMachine.make((CoffeeType) stateContext.getMessageHeader("coffee_type"));
        stateContext.getExtendedState().getVariables().put("info", "Start making coffee");
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
