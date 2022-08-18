package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;

import net.coffeemachine.model.Supplies;
import net.coffeemachine.model.coffee.CoffeeRecipe;
import net.coffeemachine.service.CoffeeMachine;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;
import net.coffeemachine.model.coffee.CoffeeType;

@Component
@RequiredArgsConstructor
public class MakeCommand extends Command {

    private final CoffeeMachine coffeeMachine;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        String info = make((CoffeeType) stateContext.getMessageHeader("coffee_type"));
        stateContext.getExtendedState().getVariables().put("info", info);
        coffeeMachine.afterTask(result -> doneIfSuccess(result, stateContext));
    }

    private String make(CoffeeType coffeeType) {
        CoffeeRecipe coffeeRecipe = coffeeMachine.recipes().get(coffeeType);
        Supplies supplies = coffeeMachine.supplies();
        if (!supplies.isEnoughFor(coffeeRecipe)) {
            return String.format("Not enough ingredients for %s: %s", coffeeRecipe.getType(), supplies.getNotEnough());
        }

        supplies.allocate(coffeeRecipe);
        coffeeMachine.startTask(coffeeRecipe.timeToMake());
        return String.format("Start making coffee %s", coffeeRecipe.getType());
    }

    @Override
    public Events getType() {
        return Events.MAKING;
    }
}
