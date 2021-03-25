package net.coffeemachine.service.states;

import net.coffeemachine.to.Info;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.CoffeeMachine;

public class ReadyState extends State {

    public ReadyState(CoffeeMachine machine) {
        super(machine);
    }

    @Override
    public Info onStart() {
        return new Info(this.toString());
    }

    @Override
    public Info onMake(CoffeeType coffeeType) {
        machine.makeCoffee(coffeeType);
        machine.changeState(new MakeState(machine));
        return new Info("Start making coffee");
    }

    @Override
    public Info onRemain() {
        return machine.remainsSupplies();
    }

    @Override
    public Info onClean() {
        machine.changeState(new CleanState(machine));
        machine.clean();
        return new Info("Start cleaning");
    }

    @Override
    public Info onStop() {
        machine.turnOf();
        machine.changeState(new StopState(machine));
        return new Info("Coffee Machine is stopping");
    }

    @Override
    public String toString() {
        return "Coffee Machine is already started";
    }
}
