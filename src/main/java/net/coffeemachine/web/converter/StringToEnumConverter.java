package net.coffeemachine.web.converter;

import org.springframework.core.convert.converter.Converter;

import net.coffeemachine.model.coffee.CoffeeType;

public class StringToEnumConverter implements Converter<String, CoffeeType> {
    @Override
    public CoffeeType convert(String source) {
        return CoffeeType.valueOf(source.toUpperCase());
    }
}
