package net.coffeemachine.service.statemachine.commands;

import org.springframework.statemachine.action.Action;

import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;

public interface Command extends Action<States, Events> {
    Events getType();
}
