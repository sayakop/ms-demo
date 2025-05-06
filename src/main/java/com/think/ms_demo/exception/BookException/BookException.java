package com.think.ms_demo.exception.BookException;

import org.springframework.http.HttpStatus;

public class BookException {

    
    private final HttpStatus httpStatus;
    private final String message;
    private final String error;

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getError() {
        return error;
    }

    public BookException(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.error = throwable != null?throwable.getLocalizedMessage() : null;
        this.httpStatus = httpStatus;
    }

}
