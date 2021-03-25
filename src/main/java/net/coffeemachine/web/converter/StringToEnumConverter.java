package net.coffeemachine.web.converter;

import net.coffeemachine.model.coffee.CoffeeType;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, CoffeeType> {
    @Override
    public CoffeeType convert(String source) {
        return CoffeeType.valueOf(source.toUpperCase());
    }
}
