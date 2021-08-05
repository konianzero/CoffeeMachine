package net.coffeemachine.model.ingredients;

import lombok.ToString;
import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.util.exception.NotEnoughSuppliesException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@ToString(callSuper = true)
public final class Supplies extends Ingredients {
    protected int cups;

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
        this.cups = water;
    }

    public void appendWater(int cups) {
        this.cups = cups;
    }

    public void appendMilk(int milk) {
        this.cups = milk;
    }

    public void appendBeans(int beans) {
        this.cups = beans;
    }

    public void allocate(Coffee coffee) {
        this.water -= coffee.getWater();
        this.milk -= coffee.getMilk();
        this.beans -= coffee.getBeans();
        this.cups -= 1;
    }

    public void check(Coffee coffee) {
        StringJoiner notEnough = new StringJoiner(", ");
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

        if (notEnough.length() != 0) {
            throw new NotEnoughSuppliesException("Not enough " + notEnough);
        }
    }
}
