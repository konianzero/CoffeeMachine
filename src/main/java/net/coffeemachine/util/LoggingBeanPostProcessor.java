package net.coffeemachine.util;

import lombok.extern.slf4j.Slf4j;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import net.coffeemachine.service.Machine;
import net.coffeemachine.service.states.State;

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
        if (bean instanceof Machine) {
            log.info("<<< postProcessAfterInitialization - " + beanName + " | " + bean.getClass().getSimpleName());
            return proxiedBean((Machine) bean);
        }
        return bean;
    }

    private Object proxiedBean(Machine bean) {
        ProxyFactory proxyFactory = new ProxyFactory(bean);
//        proxyFactory.addInterface(Machine.class);
        proxyFactory.addAdvice(new LoggingInterceptor());
        return proxyFactory.getProxy();
    }

    private static class LoggingInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Object result = methodInvocation.proceed();
            log.info("BPP <<< Invoked: {} - Returned: {}", methodInvocation.getMethod().getName(), result);
            return result;
        }
    }
}
