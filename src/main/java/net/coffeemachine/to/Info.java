package net.coffeemachine.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(name="Information response", description="Simple information response")
public class Info {
    private Object message;
}
