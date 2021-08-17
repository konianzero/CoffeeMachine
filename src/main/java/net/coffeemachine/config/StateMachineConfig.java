package net.coffeemachine.config;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;
import net.coffeemachine.service.statemachine.commands.Commands;

@Configuration
@EnableStateMachine
@RequiredArgsConstructor
@Slf4j
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    private final Commands commands;

    @Override
    public void configure(
            StateMachineConfigurationConfigurer
                    <States, Events> config) throws Exception {

        config.withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(
            StateMachineStateConfigurer<States, Events> states)
            throws Exception {

        states.withStates()
                .initial(States.READY)
                .state(States.IDLE)
                .state(States.MAKE)
                .state(States.CLEAN);
    }

    @Override
    public void configure(
            StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {

        transitions
                .withExternal()
                    .source(States.IDLE)
                    .target(States.READY)
                    .event(Events.STARTING)
                    .action(commands.starting())
//                    .action(commandsMap.get(Events.STARTING))
                    .and()
                .withExternal()
                    .source(States.READY)
                    .target(States.MAKE)
                    .event(Events.MAKING)
                    .action(commands.making())
                    .and()
                .withExternal()
                    .source(States.READY)
                    .target(States.CLEAN)
                    .event(Events.CLEANING)
                    .action(commands.cleaning())
                    .and()
                .withExternal()
                    .source(States.MAKE)
                    .target(States.READY)
                    .event(Events.DONE)
                    .and()
                .withExternal()
                    .source(States.CLEAN)
                    .target(States.READY)
                    .event(Events.DONE)
                    .and()
                .withExternal()
                    .source(States.READY)
                    .target(States.IDLE)
                    .event(Events.STOPPING)
                    .action(commands.stopping())
//                    .action(commandsMap.get(Events.STOPPING));
                    .and()
                .withInternal()
                    .source(States.READY)
                    .event(Events.REMAINING)
                    .action(commands.remaining());
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {

            @Override
            public void transition(Transition<States, Events> transition) {
                log.warn(">>> Transition from:{} to:{}",
                        ofNullableState(transition.getSource()),
                        ofNullableState(transition.getTarget()));
            }

            @Override
            public void eventNotAccepted(Message<Events> event) {
                log.error(">>> Event not accepted: {}", event);
            }

            private Object ofNullableState(State<States, Events> s) {
                return Optional.ofNullable(s)
                        .map(State::getId)
                        .orElse(null);
            }
        };
    }
}
