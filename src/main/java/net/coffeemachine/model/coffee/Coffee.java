package net.coffeemachine.model.coffee;

import net.coffeemachine.service.CoffeeMachine;

public abstract class Coffee {
    private CoffeeMachine coffeeMachine;
    protected String name;
    protected int timeToMake;
    protected int water;
    protected int milk;
    protected int beans;

    public Coffee(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    public void prepare() {
        coffeeMachine.allocateSupplies(water, milk, beans);
    }

    public String getName() {
        return name;
    }

    public int getTimeToMake() {
        return timeToMake;
    }
}
