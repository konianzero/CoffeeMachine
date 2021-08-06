package net.coffeemachine.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import net.coffeemachine.model.coffee.Coffee;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.states.State;
import net.coffeemachine.service.states.StateType;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public Map<CoffeeType, Coffee> coffeeFactory(List<Coffee> coffeeList) {
        log.info("Start coffee factory");
        return coffeeList.stream().collect(Collectors.toMap(Coffee::getType, Function.identity()));
    }

    @Bean
    public Map<StateType, State> coffeeMachineState(List<State> states) {
        log.info("Initializing coffee machine states");
        return states.stream().collect(Collectors.toMap(State::getType, Function.identity()));
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ExecutorService coffeeMachineService() {
        log.info("Start coffee machine service");
        return Executors.newSingleThreadExecutor();
    }
}
