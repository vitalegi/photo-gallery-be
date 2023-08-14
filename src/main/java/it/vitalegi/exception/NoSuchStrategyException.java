package it.vitalegi.exception;

public class NoSuchStrategyException extends RuntimeException {
    public NoSuchStrategyException() {
    }

    public NoSuchStrategyException(String message) {
        super(message);
    }

    public NoSuchStrategyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchStrategyException(Throwable cause) {
        super(cause);
    }

    public NoSuchStrategyException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
