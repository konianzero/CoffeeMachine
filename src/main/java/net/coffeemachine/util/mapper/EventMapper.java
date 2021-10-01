package net.coffeemachine.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;

import net.coffeemachine.commands.ActionType;
import net.coffeemachine.config.StateMachineConfig.Events;

@Mapper
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @ValueMapping(source = "START", target = "STARTING")
    @ValueMapping(source = "MAKE", target = "MAKING")
    @ValueMapping(source = "CLEAN", target = "CLEANING")
    @ValueMapping(source = "REMAIN", target = "REMAINING")
    @ValueMapping(source = "STOP", target = "STOPPING")
    Events toEventFrom(ActionType actionType);
}
