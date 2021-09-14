package net.coffeemachine.config;

import lombok.extern.slf4j.Slf4j;

import net.coffeemachine.service.statemachine.Events;
import net.coffeemachine.service.statemachine.States;
import org.h2.tools.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.sql.SQLException;
import java.util.Optional;

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
