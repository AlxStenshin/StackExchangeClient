package ru.alxstn.stackexchangeclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DataProviderException extends ResponseStatusException {
    public DataProviderException(HttpStatus status, String message) {
        super(status, message);
    }
}
