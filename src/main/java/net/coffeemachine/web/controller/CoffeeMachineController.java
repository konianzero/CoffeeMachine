package net.coffeemachine.web.controller;

import net.coffeemachine.to.Info;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.CoffeeMachine;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = CoffeeMachineController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CoffeeMachineController {
    public static final String REST_URL = "/control";

    private final CoffeeMachine coffeeMachine;

    public CoffeeMachineController(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

//    @GetMapping("/start")
    @PatchMapping("/start")
    public Info start() {
        return coffeeMachine.getState().onStart();
    }

//    @GetMapping("/make")
    @PatchMapping("/make")
    public Info make(@RequestParam("coffeeType") CoffeeType coffeeType) {
        return coffeeMachine.getState().onMake(coffeeType);
    }

//    @GetMapping("/remains")
    @PatchMapping("/remains")
    public Info remains() {
        return coffeeMachine.getState().onRemain();
    }

//    @GetMapping("/clean")
    @PatchMapping("/clean")
    public Info clean() {
        return coffeeMachine.getState().onClean();
    }

//    @GetMapping("/stop")
    @PatchMapping("/stop")
    public Info stop() {
        return coffeeMachine.getState().onStop();
    }
}
