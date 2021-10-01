package net.coffeemachine.service;

import lombok.RequiredArgsConstructor;

import net.coffeemachine.commands.ActionType;
import net.coffeemachine.commands.ObjectFactory;
import net.coffeemachine.commands.Response;
import net.coffeemachine.util.mapper.EventMapper;
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
    private static final EventMapper eventMapper = EventMapper.INSTANCE;

    private final StateMachine<States, Events> stateMachine;
    private final ObjectFactory objectFactory;

    public Response processAction(ActionType action) {
        sendEvent(eventMapper.toEventFrom(action));
        return makeResponse(getStateInfo());
    }

    public void sendEvent(Events event) {
        sendEvent(MessageBuilder.withPayload(event).build());
    }

    public void sendEvent(Message<Events> message) {
        stateMachine.sendEvent(Mono.just(message)).subscribe();
    }

    public String getStateInfo() {
        return (String) stateMachine.getExtendedState().getVariables().get("info");
    }

    public Response makeResponse(String text) {
        Response response = objectFactory.createResponse();
        response.setMessage(text);
        return response;
    }
}
