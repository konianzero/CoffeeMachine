package net.coffeemachine.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

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

    private final Machine coffeeMachine;

    @PatchMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public Info start() {
        return new Info(coffeeMachine.getState().onStart());
    }

    @PatchMapping("/make")
    public Info make(@RequestParam("coffeeType") CoffeeType coffeeType) {
        return new Info(coffeeMachine.getState().onMake(coffeeType));
    }

    @PatchMapping("/remains")
    public Info remains() {
        return new Info(coffeeMachine.getState().onRemain());
    }

    @PatchMapping("/clean")
    public Info clean() {
        return new Info(coffeeMachine.getState().onClean());
    }

    @PatchMapping("/stop")
    public Info stop() {
        return new Info(coffeeMachine.getState().onStop());
    }
}
