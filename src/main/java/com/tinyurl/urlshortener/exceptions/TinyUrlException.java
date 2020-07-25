package com.tinyurl.urlshortener.exceptions;

public class TinyUrlException extends Exception {

    protected TinyUrlError tinyUrlError;
    public TinyUrlException(TinyUrlError tinyUrlError) {
        super(tinyUrlError.errorCode + "-- " + tinyUrlError.getErrorMsg());
        this.tinyUrlError = tinyUrlError;
    }
}
