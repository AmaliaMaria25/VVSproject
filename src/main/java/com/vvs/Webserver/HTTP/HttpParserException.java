package com.vvs.Webserver.HTTP;

public class HttpParserException extends Exception{
    private StatusCode errorCode;

    public HttpParserException(StatusCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public StatusCode getErrorCode() {
        return errorCode;
    }
}
