package net.coffeemachine.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import net.coffeemachine.service.CoffeeMachineCommands;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import net.coffeemachine.to.Info;
import net.coffeemachine.service.Machine;
import net.coffeemachine.model.coffee.CoffeeType;

@RestController
@AllArgsConstructor
@Tag(name = "CoffeeMachine Controller")
@RequestMapping(value = CoffeeMachineController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CoffeeMachineController {
    public static final String REST_URL = "/control";

    private final CoffeeMachineCommands commands;

    @PatchMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public Info start() {
        return new Info(commands.start());
    }

    @PatchMapping("/make")
    public Info make(@RequestParam("coffeeType") CoffeeType coffeeType) {
        return new Info(commands.make(coffeeType));
    }

    @PatchMapping("/remains")
    public Info remains() {
        return new Info(commands.remains());
    }

    @PatchMapping("/clean")
    public Info clean() {
        return new Info(commands.clean());
    }

    @PatchMapping("/stop")
    public Info stop() {
        return new Info(commands.stop());
    }
}
