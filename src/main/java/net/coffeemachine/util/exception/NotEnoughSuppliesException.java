package net.coffeemachine.util.exception;

public class NotEnoughSuppliesException extends RuntimeException {
    public NotEnoughSuppliesException(String message) {
        super(message);
    }
}
