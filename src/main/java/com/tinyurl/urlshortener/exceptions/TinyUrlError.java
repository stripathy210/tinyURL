package com.tinyurl.urlshortener.exceptions;

public enum TinyUrlError {

    NO_SHORT_URL_FOUND_ERROR("T001", "No Short URL Found"),
    UNABLE_TO_LOCK("T002", "Servers are busy, Try after some time"),
    ERROR_ALLOCATE_MEMORY("T003", "Out of Range"),
    INVALID_REQ_ERROR("T004", "Invalid Request");

    String errorCode;
    String msg;

    TinyUrlError(String errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return msg;
    }
}
