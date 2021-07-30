package net.coffeemachine.model.coffee;

public class Latte extends Coffee {
    public Latte() {
        name = CoffeeType.LATTE.name().toLowerCase();
        timeToMake = 30000;
        water = 350;
        milk = 75;
        beans = 20;
    }
}
