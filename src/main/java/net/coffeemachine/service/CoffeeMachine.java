package net.coffeemachine.service;

import net.coffeemachine.model.Supplies;
import net.coffeemachine.model.coffee.CoffeeRecipe;
import net.coffeemachine.model.coffee.CoffeeType;

import java.util.Map;

public interface CoffeeMachine extends Machine {
    Supplies supplies();
    Map<CoffeeType, CoffeeRecipe> recipes();
}
