package net.coffeemachine.util;

import lombok.extern.slf4j.Slf4j;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import net.coffeemachine.service.Machine;
import net.coffeemachine.util.aspect.DatabaseLogging;

@Slf4j
@Component
public class LoggingBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Class> map = new HashMap<>();

    private void infoLog(String prefix, Object bean, String beanName) {
        log.info(prefix + " Class name: " + bean.getClass().getSimpleName());
        log.info(prefix + " Bean name: " + beanName);
        log.info(prefix + " All annotations: " + Arrays.stream(bean.getClass().getAnnotations()).map(Annotation::toString).collect(Collectors.joining(", ")));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        /* No annotations presented because of @Lookup on CoffeeMachineEquipment.startEquipment() !!!
        if (bean.getClass().isAnnotationPresent(DatabaseLogging.class)) {
         */
        if (bean instanceof Machine) {
            infoLog("---", bean, beanName);
            map.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = map.get(beanName);
        if (beanClass != null) {
            infoLog("***", bean, beanName);
            return proxyBeanWithProxyFactory(bean);
            /* !!! Produce error !!!
            return proxyBeanWithNewInstance(bean);
             */
        }
        return bean;
    }

    private Object proxyBeanWithProxyFactory(Object bean) {
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

    /*
     !!! Produce error !!!
        Bean named 'coffeeMachineEquipment' is expected to be of type 'net.coffeemachine.service.Machine'
        but was actually of type 'org.springframework.cglib.proxy.Proxy$ProxyImpl$$EnhancerByCGLIB$$dafaefd6'
     */
    private Object proxyBeanWithNewInstance(Object bean) {
        Class<?> beanClass = bean.getClass();

        if (beanClass != null) {
            return Proxy.newProxyInstance(
                    beanClass.getClassLoader(),
                    beanClass.getInterfaces(),
                    (o, method, args) -> {
                        Object result = method.invoke(bean, args);
                        log.info("{}", result);
                        return result;
                    }
            );
        }
        return bean;
    }
}
