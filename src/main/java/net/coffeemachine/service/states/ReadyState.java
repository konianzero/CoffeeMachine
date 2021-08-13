package net.coffeemachine.service.states;

import org.springframework.stereotype.Component;

import net.coffeemachine.model.coffee.CoffeeType;

@Component
public class ReadyState extends AbstractState {

    @Override
    public String onMake(CoffeeType coffeeType) {
        return machine.make(coffeeType);
    }

    @Override
    public String onRemain() {
        return machine.remainsSupplies();
    }

    @Override
    public String onClean() {
        return machine.clean();
    }

    @Override
    public String onStop() {
        return machine.turnOff();
    }

    @Override
    public StateType getType() {
        return StateType.READY;
    }

    public String getInfo() {
        return "Coffee Machine is already started";
    }
}
