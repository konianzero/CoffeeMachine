package net.coffeemachine.to;

import lombok.Getter;

@Getter
public class Info {
    private String message;

    public Info(String message) {
        this.message = message;
    }
}
