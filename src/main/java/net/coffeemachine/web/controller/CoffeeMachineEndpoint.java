package net.coffeemachine.web.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.coffeemachine.commands.Action;
import net.coffeemachine.commands.ActionType;
import net.coffeemachine.commands.ObjectFactory;
import net.coffeemachine.commands.Response;
import net.coffeemachine.config.StateMachineConfig;
import net.coffeemachine.service.CoffeeMachine;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Slf4j
@Endpoint
public class CoffeeMachineEndpoint {

    private static final String NAMESPACE_URI = "http://coffeemachine.net/commands";

    private final CoffeeMachine coffeeMachine;
    private ObjectFactory objectFactory;

    @PostConstruct
    private void init() {
         objectFactory = new ObjectFactory();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "Action")
    @ResponsePayload
    public Response start(@RequestPayload Action input) {
        Response response = objectFactory.createResponse();
        log.info("RequestPayload input action type: {}", input.getActionType());
        if (input.getActionType().equals(ActionType.START)) {
            coffeeMachine.sendEvent(StateMachineConfig.Events.STARTING);
            response.setMessage("Starting");
        }
        return response;
    }
}
