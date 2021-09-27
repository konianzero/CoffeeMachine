package net.coffeemachine.model;

import java.util.StringJoiner;

import lombok.ToString;

import net.coffeemachine.model.coffee.CoffeeRecipe;
import net.coffeemachine.model.ingredients.Ingredients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@ToString(callSuper = true)
public final class Supplies extends Ingredients {
    protected int cups;
    @ToString.Exclude
    protected StringJoiner notEnough;

    public Supplies(@Value("${coffee-machine.cups}") int cups,
                    @Value("${coffee-machine.water}") int water,
                    @Value("${coffee-machine.milk}") int milk,
                    @Value("${coffee-machine.beans}") int beans) {
        this.cups = cups;
        this.water = water;
        this.milk = milk;
        this.beans = beans;
    }

    public void appendCups(int water) {
        this.water = water;
    }

    public void appendWater(int cups) {
        this.cups = cups;
    }

    public void appendMilk(int milk) {
        this.milk = milk;
    }

    public void appendBeans(int beans) {
        this.beans = beans;
    }

    public String getNotEnough() {
        return notEnough != null ? notEnough.toString() : "";
    }

    public void allocate(CoffeeRecipe coffeeRecipe) {
        this.water -= coffeeRecipe.getWater();
        this.milk -= coffeeRecipe.getMilk();
        this.beans -= coffeeRecipe.getBeans();
        this.cups -= 1;
    }

    public boolean isEnoughFor(CoffeeRecipe coffeeRecipe) {
        notEnough = new StringJoiner(", ");
        if (this.water - coffeeRecipe.getWater() < 0) {
            notEnough.add("water");
        }
        if (this.milk - coffeeRecipe.getMilk() < 0) {
            notEnough.add("milk");
        }
        if (this.beans - coffeeRecipe.getBeans() < 0) {
            notEnough.add("beans");
        }
        if (this.cups - 1 < 0) {
            notEnough.add("cups");
        }

        return notEnough.length() == 0;
    }
}
