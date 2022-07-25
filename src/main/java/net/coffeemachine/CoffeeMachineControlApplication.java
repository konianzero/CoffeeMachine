package net.coffeemachine;

import lombok.RequiredArgsConstructor;
import net.coffeemachine.config.StateMachineConfig;
import net.coffeemachine.service.statemachine.commands.Command;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

import java.util.Map;

@SpringBootApplication
@RequiredArgsConstructor
public class CoffeeMachineControlApplication implements CommandLineRunner {

    private final StateMachine<StateMachineConfig.States, StateMachineConfig.Events> stateMachine;

    @Override
    public void run(String... args) {
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(StateMachineConfig.Events.STARTING).build())).subscribe();
    }

    public static void main(String[] args) {
        SpringApplication.run(CoffeeMachineControlApplication.class, args);
    }
}
