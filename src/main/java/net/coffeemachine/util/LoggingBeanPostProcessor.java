package net.coffeemachine.util;

import lombok.extern.slf4j.Slf4j;

import net.coffeemachine.service.CoffeeMachineCommands;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@Order(0)
public class LoggingBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CoffeeMachineCommands) {
            return proxiedBean((CoffeeMachineCommands) bean);
        }
        return bean;
    }

    private Object proxiedBean(CoffeeMachineCommands bean) {
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvice(new LoggingInterceptor());
        return proxyFactory.getProxy();
    }

    private static class LoggingInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Object result = methodInvocation.proceed();
            log.info("{}", result);
            return result;
        }
    }
}
