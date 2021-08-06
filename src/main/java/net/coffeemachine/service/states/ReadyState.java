package net.coffeemachine.service.states;

import org.springframework.stereotype.Component;

import net.coffeemachine.to.Info;
import net.coffeemachine.model.coffee.CoffeeType;

@Component
public class ReadyState extends State {

    @Override
    public Info onMake(CoffeeType coffeeType) {
        return new Info(machine.makeCoffee(coffeeType));
    }

    @Override
    public Info onRemain() {
        return new Info(machine.remainsSupplies());
    }

    @Override
    public Info onClean() {
        return new Info(machine.clean());
    }

    @Override
    public Info onStop() {
        return new Info(machine.turnOf());
    }

    @Override
    public StateType getType() {
        return StateType.READY;
    }

    public String getInfo() {
        return "Coffee Machine is already started";
    }
}
