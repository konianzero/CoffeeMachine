package net.coffeemachine.service;

import lombok.RequiredArgsConstructor;

import net.coffeemachine.commands.Action;
import net.coffeemachine.commands.ActionType;
import net.coffeemachine.commands.ObjectFactory;
import net.coffeemachine.commands.Response;
import net.coffeemachine.util.mapper.MapStructMapper;
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
@RequiredArgsConstructor
public class CoffeeMachine {

    private final StateMachine<States, Events> stateMachine;
    private final ObjectFactory objectFactory;
    private final MapStructMapper mapper;

    public Response processAction(Action action) {
        if (action.getActionType().equals(ActionType.MAKE)) {
            sendEvent(
                    MessageBuilder
                    .withPayload(Events.MAKING)
                    .setHeader("coffee_type", mapper.mapCoffeeType(action.getCoffeeType()))
                    .build()
            );
        } else {
            sendEvent(mapper.toEventFrom(action.getActionType()));
        }
        return makeResponse(getStateInfo());
    }

    private void sendEvent(Events event) {
        sendEvent(MessageBuilder.withPayload(event).build());
    }

    private void sendEvent(Message<Events> message) {
        stateMachine.sendEvent(Mono.just(message)).subscribe();
    }

    private String getStateInfo() {
        return (String) stateMachine.getExtendedState().getVariables().get("info");
    }

    private Response makeResponse(String text) {
        Response response = objectFactory.createResponse();
        response.setMessage(text);
        return response;
    }
}
