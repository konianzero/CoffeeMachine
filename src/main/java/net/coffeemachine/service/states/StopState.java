package net.coffeemachine.service.states;

import org.springframework.stereotype.Component;

/**
 * @deprecated (use states from StateMachine)
 */
@Deprecated(since = "2.1.2")
@Component
public class StopState extends AbstractState {

    @Override
    public String onStart() {
        return machine.turnOn();
    }

    @Override
    public StateType getType() {
        return StateType.STOP;
    }

    public String getInfo() {
        return "Coffee Machine is stopped";
    }
}
