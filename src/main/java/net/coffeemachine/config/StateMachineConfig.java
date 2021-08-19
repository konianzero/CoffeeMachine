package net.coffeemachine.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;
import net.coffeemachine.service.statemachine.commands.Command;

@Configuration
@EnableStateMachine
@RequiredArgsConstructor
@Slf4j
public class StateMachineConfig {

    @Bean
    @DependsOn("commandsMap")
    public StateMachine<States, Events> stateMachine(
            StateMachineListener<States, Events> listener,
            Map<Events, Command> commandsMap
    ) throws Exception {

        StateMachineBuilder.Builder<States, Events> builder = new StateMachineBuilder.Builder<>();

        log.info("Configuring configuration");
        builder.configureConfiguration()
                .withConfiguration()
                .autoStartup(true)
                .listener(listener);

        log.info("Configuring states");
        builder.configureStates()
                .withStates()
                    .initial(States.READY, commandsMap.get(Events.STARTING))
                    .state(States.IDLE)
                    .state(States.MAKE)
                    .state(States.CLEAN);

        log.info("Configuring transitions");
        builder.configureTransitions()
                .withExternal()
                    .source(States.IDLE)
                    .target(States.READY)
                    .event(Events.STARTING)
                    .action(commandsMap.get(Events.STARTING))
                    .and()
                .withExternal()
                    .source(States.READY)
                    .target(States.MAKE)
                    .event(Events.MAKING)
                    .action(commandsMap.get(Events.MAKING))
                    .and()
                .withExternal()
                    .source(States.READY)
                    .target(States.CLEAN)
                    .event(Events.CLEANING)
                    .action(commandsMap.get(Events.CLEANING))
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
                    .action(commandsMap.get(Events.STOPPING))
                    .and()
                .withInternal()
                    .source(States.READY)
                    .event(Events.REMAINING)
                    .action(commandsMap.get(Events.REMAINING));

        return builder.build();
    }

    @Bean
    @Profile("prod")
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {

            @Override
            public void eventNotAccepted(Message<Events> event) {
                log.error("Event not accepted: {}", event.getPayload());
            }
        };
    }

    @Bean
    public Map<Events, Command> commandsMap(List<Command> commands) {
        log.info("Initializing coffee machine commands");
        return commands.stream().collect(Collectors.toMap(Command::getType, Function.identity()));
    }
}
