package net.coffeemachine.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import net.coffeemachine.service.CoffeeMachine;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;

import net.coffeemachine.to.Info;
import net.coffeemachine.service.Machine;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;

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
        if (stateMachine.sendEvent(Events.STARTING)) {
            coffeeMachine.turnOn();
        }
        return new Info("Starting");
    }

    @PatchMapping("/make")
    public Info make(@RequestParam("coffeeType") CoffeeType coffeeType) {
        if (stateMachine.sendEvent(MessageBuilder.withPayload(Events.PROCESSING).setHeader("coffee_type", coffeeType).build())) {
            coffeeMachine.make(coffeeType);
            ((CoffeeMachine) coffeeMachine).getRunningTask()
                    .thenAccept(result -> {
                        if (result) {
                            stateMachine.sendEvent(Events.DONE);
                        }
                    });
        }
        return new Info("Making Coffee");
    }

    @PatchMapping("/remains")
    public Info remains() {
        // TODO - Refactor remain supplies
        return new Info("Remaining NOT SUPPORTED");
    }

    @PatchMapping("/clean")
    public Info clean() {
        if (stateMachine.sendEvent(Events.PROCESSING)) {
            coffeeMachine.clean();
            ((CoffeeMachine) coffeeMachine).getRunningTask()
                    .thenAccept(result -> {
                        if (result) {
                            stateMachine.sendEvent(Events.DONE);
                        }
                    });
        }
        return new Info("Cleaning Machine");
    }

    @PatchMapping("/stop")
    public Info stop() {
        if (stateMachine.sendEvent(Events.STOPPING)) {
            coffeeMachine.turnOff();
        }
        return new Info("Stopping");
    }
}
