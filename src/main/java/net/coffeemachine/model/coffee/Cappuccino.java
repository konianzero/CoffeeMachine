package net.coffeemachine.model.coffee;

public class Cappuccino extends Coffee {
    public Cappuccino() {
        name = CoffeeType.CAPPUCCINO.name().toLowerCase();
        timeToMake = 45000;
        water = 200;
        milk = 100;
        beans = 12;
    }
}
