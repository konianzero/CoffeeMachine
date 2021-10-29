package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import net.coffeemachine.service.CoffeeMachineEquipment;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import net.coffeemachine.service.Machine;
import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;

@Component
@RequiredArgsConstructor
public class CleanCommand implements Command {

    private final Machine coffeeMachine;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        String info = coffeeMachine.clean();
        stateContext.getExtendedState().getVariables().put("info", info);
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
        return Events.CLEANING;
    }
}
