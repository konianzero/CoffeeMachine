package net.coffeemachine.util.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.coffeemachine.config.StateMachineConfig;
import org.aspectj.lang.annotation.*;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseLoggingAspect {
    private final Marker ldb = MarkerFactory.getMarker("LDB");
    private final StateMachine<StateMachineConfig.States, StateMachineConfig.Events> stateMachine;

    @Pointcut("@annotation(net.coffeemachine.util.aspect.LogToDB)")
    public void loggableMethods() {
    }

    @AfterReturning("loggableMethods()")
    public void logAfterReturningAllMethods() throws Throwable {
        String info = (String) stateMachine.getExtendedState().getVariables().get("info");
        log.info(ldb, info);
    }

    @AfterThrowing(pointcut = "execution(* net.coffeemachine.service..*(..))", throwing = "ex")
    public void logAfterThrowingAllMethods(Exception ex) {
        log.error(ex.getMessage());
    }

}
