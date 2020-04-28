package com.freakycoder.casino.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected ResponseEntity<String> error(Throwable t) {
        if (t instanceof IllegalArgumentException) {
            return ResponseEntity.badRequest().body(t.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(t.getMessage());
        }
    }

    protected <T> ResponseEntity<T> success(T value) {
        return ResponseEntity.ok(value);
    }
}
