package net.coffeemachine.web.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.coffeemachine.commands.Action;
import net.coffeemachine.commands.Response;

import net.coffeemachine.service.CoffeeMachineService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@RequiredArgsConstructor
@Slf4j
@Endpoint
public class CoffeeMachineEndpoint {

    private static final String NAMESPACE_URI = "http://coffeemachine.net/commands";

    private final CoffeeMachineService coffeeMachineService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "Action")
    @ResponsePayload
    public Response action(@RequestPayload Action action) {
        return coffeeMachineService.processAction(action);
    }
}
