package com.vvs.Webserver.Utils;

public class StoppedServerException extends RuntimeException{
    public StoppedServerException() { }

    public StoppedServerException(String message) {
        super(message);
    }

    public StoppedServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoppedServerException(Throwable cause) {
        super(cause);
    }
}
