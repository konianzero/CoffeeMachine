package net.coffeemachine.service.states;

import org.springframework.stereotype.Component;

/**
 * @deprecated (use states from StateMachine)
 */
@Deprecated(since = "2.1.2")
@Component
public class CleanState extends AbstractState {

    @Override
    public String onStop() {
        return machine.turnOff();
    }

    @Override
    public String onRemain() {
        return machine.remainsSupplies();
    }

    @Override
    public StateType getType() {
        return StateType.CLEAN;
    }

    public String getInfo() {
        return "Coffee Machine is cleaning, please wait.";
    }
}
