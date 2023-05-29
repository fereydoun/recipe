package com.abn.recipe.exceptions;

import org.springframework.http.HttpStatus;

public interface IException {
    HttpStatus getHttpStatus();
}