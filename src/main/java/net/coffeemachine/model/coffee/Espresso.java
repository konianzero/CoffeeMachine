package net.coffeemachine.model.coffee;

public class Espresso extends Coffee {
    public Espresso() {
        name = CoffeeType.ESPRESSO.name().toLowerCase();
        timeToMake = 20000;
        water = 250;
        beans = 16;
    }
}
