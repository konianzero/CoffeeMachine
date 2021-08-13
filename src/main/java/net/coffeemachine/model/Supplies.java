package net.coffeemachine.model;

import java.util.StringJoiner;

import lombok.ToString;

import net.coffeemachine.model.ingredients.Ingredients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.coffeemachine.model.coffee.Coffee;

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

    public void allocate(Coffee coffee) {
        this.water -= coffee.getWater();
        this.milk -= coffee.getMilk();
        this.beans -= coffee.getBeans();
        this.cups -= 1;
    }

    public boolean isEnoughFor(Coffee coffee) {
        notEnough = new StringJoiner(", ");
        if (this.water - coffee.getWater() < 0) {
            notEnough.add("water");
        }
        if (this.milk - coffee.getMilk() < 0) {
            notEnough.add("milk");
        }
        if (this.beans - coffee.getBeans() < 0) {
            notEnough.add("beans");
        }
        if (this.cups - 1 < 0) {
            notEnough.add("cups");
        }

        return notEnough.length() == 0;
    }
}
