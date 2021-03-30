package net.coffeemachine.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import net.coffeemachine.to.Info;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.CoffeeMachine;

@RestController
@RequestMapping(value = CoffeeMachineController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CoffeeMachineController {
    public static final String REST_URL = "/control";

    private final CoffeeMachine coffeeMachine;

    public CoffeeMachineController(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    @PatchMapping("/start")
    public Info start() {
        return coffeeMachine.getState().onStart();
    }

    @PatchMapping("/make")
    public Info make(@RequestParam("coffeeType") CoffeeType coffeeType) {
        return coffeeMachine.getState().onMake(coffeeType);
    }

    @PatchMapping("/remains")
    public Info remains() {
        return coffeeMachine.getState().onRemain();
    }

    @PatchMapping("/clean")
    public Info clean() {
        return coffeeMachine.getState().onClean();
    }

    @PatchMapping("/stop")
    public Info stop() {
        return coffeeMachine.getState().onStop();
    }
}
