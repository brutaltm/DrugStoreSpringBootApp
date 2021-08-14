package edu.uph.ii.springbootprj.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such order")
public class UserNotFoundOrAlreadyActivatedException extends RuntimeException {
    public UserNotFoundOrAlreadyActivatedException(String code) {
        super("Kod aktywacyjny " + code + " nie jest przypisany do żadnego konta lub konto zostało już aktywowane.");
    }
}
