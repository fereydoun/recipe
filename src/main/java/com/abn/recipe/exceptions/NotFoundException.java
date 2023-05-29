package com.abn.recipe.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException implements IException{
    private HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}