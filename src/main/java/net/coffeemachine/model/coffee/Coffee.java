package net.coffeemachine.model.coffee;

import lombok.Getter;

@Getter
public abstract class Coffee {
    protected int timeToMake;
    protected int water;
    protected int milk;
    protected int beans;

    public abstract CoffeeType getType();
}
