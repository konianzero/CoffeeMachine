package net.coffeemachine.util.mapper;

import net.coffeemachine.model.coffee.CoffeeType;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

import net.coffeemachine.commands.ActionType;
import net.coffeemachine.config.StateMachineConfig.Events;

@Mapper(componentModel = "spring")
public interface MapStructMapper {

    @ValueMapping(source = "START", target = "STARTING")
    @ValueMapping(source = "MAKE", target = "MAKING")
    @ValueMapping(source = "CLEAN", target = "CLEANING")
    @ValueMapping(source = "REMAIN", target = "REMAINING")
    @ValueMapping(source = "STOP", target = "STOPPING")
    Events toEventFrom(ActionType actionType);

    CoffeeType mapCoffeeType(net.coffeemachine.commands.CoffeeType coffeeType);
}
