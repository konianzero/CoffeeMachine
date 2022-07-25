package net.coffeemachine.util.aspect;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DatabaseLoggingAspect {
    private final Marker inv = MarkerFactory.getMarker("INV");

    @Pointcut("@annotation(net.coffeemachine.util.aspect.LogToDB)")
    public void loggableMethods() {
    }

    @Around("loggableMethods()")
    public Object logCoffeeMachineActions(ProceedingJoinPoint joinPoint) throws Throwable {
        Object value = joinPoint.proceed();
        log.info(inv, (String) value);
        return value;
    }

    @AfterThrowing(pointcut = "execution(* net.coffeemachine.service..*(..))", throwing = "ex")
    public void logAfterThrowingAllMethods(Exception ex) {
        log.error(ex.getMessage());
    }

}
