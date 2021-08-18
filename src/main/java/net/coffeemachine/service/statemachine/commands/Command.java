package net.coffeemachine.service.statemachine.commands;

import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;
import org.springframework.statemachine.action.Action;

public interface Command extends Action<States, Events> {
    Events getType();
}
