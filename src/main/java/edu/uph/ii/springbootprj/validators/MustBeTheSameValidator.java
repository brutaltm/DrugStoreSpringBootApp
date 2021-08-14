package edu.uph.ii.springbootprj.validators;

import edu.uph.ii.springbootprj.domain.Drug;
import edu.uph.ii.springbootprj.domain.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MustBeTheSameValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(User.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        var user = (User) o;
        if (!user.getPasswordConfirm().equals(user.getPassword()))
            errors.rejectValue("passwordConfirm", "PasswordsNotTheSame");
    }
}
