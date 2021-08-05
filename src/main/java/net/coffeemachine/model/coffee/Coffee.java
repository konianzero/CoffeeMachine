package net.coffeemachine.model.coffee;

import lombok.Getter;
import net.coffeemachine.model.ingredients.Ingredients;

@Getter
public abstract class Coffee extends Ingredients {
    protected int timeToMake;

    public abstract CoffeeType getType();
}
