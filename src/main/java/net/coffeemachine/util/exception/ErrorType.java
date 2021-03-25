package net.coffeemachine.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    APP_ERROR("Application error",HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_ENOUGH_SUPPLIES("Not enough supplies", HttpStatus.UNPROCESSABLE_ENTITY),
    WRONG_REQUEST("Wrong request", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final HttpStatus status;

    ErrorType(String errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
