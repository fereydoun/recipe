package com.abn.recipe.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException implements IException{
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BadRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}