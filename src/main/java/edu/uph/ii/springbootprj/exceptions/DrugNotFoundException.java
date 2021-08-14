package edu.uph.ii.springbootprj.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such order")
public class DrugNotFoundException extends RuntimeException {
    public DrugNotFoundException(Long id) {
        super("Lek o id " + id + " nie istnieje.");
    }
}
