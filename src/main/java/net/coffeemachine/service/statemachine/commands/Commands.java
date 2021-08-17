package net.coffeemachine.service.statemachine.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.Machine;
import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;

@Component
@RequiredArgsConstructor
@Slf4j
public class Commands {

    private final Machine coffeeMachine;

    // TODO - Move actions in separate classes
    public Action<States, Events> starting() {
        return context -> coffeeMachine.turnOn();
    }

    public Action<States, Events> stopping() {
        return context -> coffeeMachine.turnOff();
    }

    public Action<States, Events> making() {
        return context -> coffeeMachine.make((CoffeeType) context.getMessageHeader("coffee_type"));
    }

    public Action<States, Events> cleaning() {
        return context -> coffeeMachine.clean();
    }

    public Action<States, Events> remaining() {
        return context -> coffeeMachine.remainsSupplies();
    }
}
