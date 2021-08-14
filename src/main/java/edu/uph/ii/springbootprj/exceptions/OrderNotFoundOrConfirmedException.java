package edu.uph.ii.springbootprj.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such order")
public class OrderNotFoundOrConfirmedException extends RuntimeException {
    public OrderNotFoundOrConfirmedException(Long id) {
        super("Zamówienie o nr id " + id + " nie istnieje.");
    }
}
