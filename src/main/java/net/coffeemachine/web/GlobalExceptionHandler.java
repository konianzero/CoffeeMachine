package net.coffeemachine.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

import net.coffeemachine.util.exception.ErrorInfo;
import net.coffeemachine.util.exception.ErrorType;
import net.coffeemachine.util.exception.NotEnoughSuppliesException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorInfo> wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetErrorInfo(req, e, ErrorType.WRONG_REQUEST);
    }

    @ExceptionHandler(NotEnoughSuppliesException.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, NotEnoughSuppliesException e) {
        return logAndGetErrorInfo(req, e, ErrorType.NOT_ENOUGH_SUPPLIES);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, ErrorType.APP_ERROR);
    }

    private ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, ErrorType errorType) {
        Throwable rootCause = logAndGetRootCause(log, req, e, errorType);
        return ResponseEntity
                .status(errorType.getStatus())
                .body(
                        new ErrorInfo(
                                req.getRequestURL(),
                                errorType,
                                errorType.getErrorCode(),
                                getMessage(rootCause))
                );
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, ErrorType errorType) {
        Throwable rootCause = getRootCause(e);
        log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        return rootCause;
    }

    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }
}
