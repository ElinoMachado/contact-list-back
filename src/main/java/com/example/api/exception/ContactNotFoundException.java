package com.example.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContactNotFoundException extends RuntimeException {
 
    public ContactNotFoundException(Long id) {
        super("Contact with ID " + id + " was not found.");
    }
}
