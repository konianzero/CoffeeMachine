package net.coffeemachine.config;

import lombok.extern.slf4j.Slf4j;

import org.h2.tools.Server;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.sql.SQLException;
import java.util.Optional;

import net.coffeemachine.config.StateMachineConfig.States;
import net.coffeemachine.config.StateMachineConfig.Events;

@Configuration
@Profile("dev")
@Slf4j
public class DevConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        log.info("Start H2 TCP server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {
            private final Marker lstn = MarkerFactory.getMarker("LSTN");

            @Override
            public void transition(Transition<States, Events> transition) {
                log.info(lstn, "Transition from:{} to:{}",
                        ofNullableState(transition.getSource()),
                        ofNullableState(transition.getTarget()));
            }

            @Override
            public void stateMachineError(StateMachine<States, Events> stateMachine, Exception exception) {
                log.error(lstn, "Exception: ", exception);
                super.stateMachineError(stateMachine, exception);
            }

            @Override
            public void eventNotAccepted(Message<Events> event) {
                log.error(lstn, "Event not accepted: {}", event);
            }

            private Object ofNullableState(State<States, Events> s) {
                return Optional.ofNullable(s)
                        .map(State::getId)
                        .orElse(null);
            }
        };
    }
}
