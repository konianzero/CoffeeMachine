package net.coffeemachine.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;

import net.coffeemachine.service.CoffeeMachine;
import net.coffeemachine.to.Info;
import net.coffeemachine.service.Machine;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Tag(name = "CoffeeMachine Controller")
@RequestMapping(value = CoffeeMachineController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CoffeeMachineController {
    public static final String REST_URL = "/control";

    private final Machine coffeeMachine;
    private final StateMachine<States, Events> stateMachine;

    @PatchMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public Info start() {
        // TODO - Refactor with new 'sendEvent' method
        //  Example:  stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.PROCESSING).build())).subscribe();
        stateMachine.sendEvent(Events.STARTING);
        return new Info("Starting");
    }

    @PatchMapping("/make")
    public Info make(@RequestParam("coffeeType") CoffeeType coffeeType) {
        if (stateMachine.sendEvent(MessageBuilder.withPayload(Events.MAKING).setHeader("coffee_type", coffeeType).build())) {
            complete();
        }
        return new Info("Making Coffee");
    }

    @PatchMapping("/remains")
    public Info remains() {
        stateMachine.sendEvent(Events.REMAINING);
        return new Info("Remaining");
    }

    @PatchMapping("/clean")
    public Info clean() {
        if (stateMachine.sendEvent(Events.CLEANING)) {
            complete();
        }
        return new Info("Cleaning Machine");
    }

    private void complete() {
        ((CoffeeMachine) coffeeMachine).getRunningTask()
                .thenAccept(result -> {
                    if (result) {
                        stateMachine.sendEvent(Events.DONE);
                    }
                });
    }

    @PatchMapping("/stop")
    public Info stop() {
        stateMachine.sendEvent(Events.STOPPING);
        return new Info("Stopping");
    }
}
