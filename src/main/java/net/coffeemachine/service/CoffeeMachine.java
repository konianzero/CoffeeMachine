package net.coffeemachine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;

@Service
@DependsOn({"coffeeMachineEquipment", "stateMachine"})
@Slf4j
@RequiredArgsConstructor
public class CoffeeMachine {

    private final StateMachine<States, Events> stateMachine;

    public void sendEvent(Events event) {
        sendEvent(MessageBuilder.withPayload(event).build());
    }

    public void sendEvent(Message<Events> message) {
        stateMachine.sendEvent(Mono.just(message)).subscribe();
    }

    public String getStateInfo() {
        return (String) stateMachine.getExtendedState().getVariables().get("info");
    }
}
