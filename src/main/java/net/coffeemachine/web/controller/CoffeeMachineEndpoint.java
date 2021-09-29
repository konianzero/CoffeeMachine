package net.coffeemachine.web.controller;

import lombok.RequiredArgsConstructor;

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
@Endpoint
public class CoffeeMachineEndpoint {

    private static final String NAMESPACE_URI = "http://coffeemachine.net/commands";

    private final CoffeeMachine coffeeMachine;
    private ObjectFactory objectFactory;

    @PostConstruct
    private void init() {
         objectFactory = new ObjectFactory();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "InputAction")
    @ResponsePayload
    public Response start(@RequestPayload Action input) {
        Response response = objectFactory.createResponse();
        if (input.getActionType().equals(ActionType.START)) {
            coffeeMachine.sendEvent(StateMachineConfig.Events.STARTING);
            response.setMessage("Starting");
        }
        return response;
    }
}
