package net.coffeemachine.util.aspect;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DatabaseLoggingAspect {


    @Pointcut("@annotation(DatabaseLogging)")
//    @Pointcut("@annotation(net.coffeemachine.util.annotation.DatabaseLogging) && execution(public * *(..))")
//    @Pointcut("within(com.example..*) && @annotation(net.coffeemachine.util.annotation.DatabaseLogging)")
//    @Pointcut("execution(* net.coffeemachine.service.CoffeeMachine.*(..))")
//    @Pointcut("execution(* net.coffeemachine.service.states..*(..))")
    public void loggableMethods() {
    }

//    @Before("loggableMethods()")
//    public void logMethod(JoinPoint jointPoint) {
//        String methodName = jointPoint.getSignature().getName();
//        log.info("<<< Before Aspect called for method: {}", methodName);
//    }

    @Around("loggableMethods()")
    public Object logCoffeeMachineActions(ProceedingJoinPoint joinPoint) throws Throwable {
        Object value = joinPoint.proceed();
        log.info("ASP <<< Invoked: {} - Returned: {}", joinPoint.getSignature().getName(), value);
        return value;
    }
}
