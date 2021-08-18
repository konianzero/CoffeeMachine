package net.coffeemachine.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import net.coffeemachine.service.CoffeeMachine;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import net.coffeemachine.to.Info;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.statemachine.Events;

@RestController
@AllArgsConstructor
@Tag(name = "CoffeeMachine Controller")
@RequestMapping(value = CoffeeMachineController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CoffeeMachineController {
    public static final String REST_URL = "/control";

    private final CoffeeMachine coffeeMachine;

    @PatchMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public Info start() {
        coffeeMachine.sendEvent(Events.STARTING);
        return new Info("Starting");
    }

    @PatchMapping("/make")
    public Info make(@RequestParam("coffeeType") CoffeeType coffeeType) {
        coffeeMachine.sendEvent(
                MessageBuilder
                        .withPayload(Events.MAKING)
                        .setHeader("coffee_type", coffeeType)
                        .build()
        );
        return new Info("Making Coffee " + coffeeType);
    }

    @PatchMapping("/remains")
    public Info remains() {
        coffeeMachine.sendEvent(Events.REMAINING);
        return new Info(coffeeMachine.getSupplies());
    }

    @PatchMapping("/clean")
    public Info clean() {
        coffeeMachine.sendEvent(Events.CLEANING);
        return new Info("Cleaning Machine");
    }

    @PatchMapping("/stop")
    public Info stop() {
        coffeeMachine.sendEvent(Events.STOPPING);
        return new Info("Stopping");
    }
}
