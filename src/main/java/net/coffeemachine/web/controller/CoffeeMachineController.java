package net.coffeemachine.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import net.coffeemachine.to.Info;
import net.coffeemachine.model.coffee.CoffeeType;
import net.coffeemachine.service.CoffeeMachineService;
import net.coffeemachine.util.WrappedResponse;
import net.coffeemachine.config.StateMachineConfig.Events;

@AllArgsConstructor
@Tag(
        name = "CoffeeMachine Controller",
        description = "Managing a coffee machine via the REST API using the following commands"
)
@RestController
@WrappedResponse
@RequestMapping(value = CoffeeMachineController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CoffeeMachineController {
    public static final String REST_URL = "/control";

    private final CoffeeMachineService coffeeMachineService;

    @Operation(method = "PATCH",
            description = "Start coffee machine equipment",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Coffee machine started",
                            content = {
                                    @Content(schema = @Schema(implementation = Info.class))
                            }
                    )
            }
    )
    @PatchMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public Info start() {
        coffeeMachineService.sendEvent(Events.STARTING);
        return new Info(coffeeMachineService.getStateInfo());
    }

    @Operation(method = "PATCH",
            description = "Start making coffee",
            parameters = {
                @Parameter(
                        in = ParameterIn.QUERY,
                        name = "coffeeType",
                        description = "Type of coffee to make"
                )
            },
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Start making coffee",
                        content = {
                                @Content(schema = @Schema(implementation = Info.class))
                        }
                )
            }
    )
    @PatchMapping("/make")
    public Info make(@RequestParam("coffeeType") CoffeeType coffeeType) {
        coffeeMachineService.sendEvent(
                MessageBuilder
                        .withPayload(Events.MAKING)
                        .setHeader("coffee_type", coffeeType)
                        .build()
        );
        return new Info(coffeeMachineService.getStateInfo());
    }

    @Operation(method = "PATCH",
            description = "Information about the remaining supplies of coffee machine",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Remaining supplies of coffee machine",
                            content = {
                                    @Content(schema = @Schema(implementation = Info.class))
                            }
                    )
            }
    )
    @PatchMapping("/remains")
    public Info remains() {
        coffeeMachineService.sendEvent(Events.REMAINING);
        return new Info(coffeeMachineService.getStateInfo());
    }

    @Operation(method = "PATCH",
            description = "Start cleaning coffee machine",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Start cleaning coffee machine",
                            content = {
                                    @Content(schema = @Schema(implementation = Info.class))
                            }
                    )
            }
    )
    @PatchMapping("/clean")
    public Info clean() {
        coffeeMachineService.sendEvent(Events.CLEANING);
        return new Info(coffeeMachineService.getStateInfo());
    }

    @Operation(method = "PATCH",
            description = "Stop coffee machine equipment",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Coffee machine stopped",
                            content = {
                                    @Content(schema = @Schema(implementation = Info.class))
                            }
                    )
            }
    )
    @PatchMapping("/stop")
    public Info stop() {
        coffeeMachineService.sendEvent(Events.STOPPING);
        return new Info(coffeeMachineService.getStateInfo());
    }
}
