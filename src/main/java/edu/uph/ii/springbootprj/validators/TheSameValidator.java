package edu.uph.ii.springbootprj.validators;

import edu.uph.ii.springbootprj.domain.Drug;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TheSameValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(Drug.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        var lek = (Drug) o;
        if (lek.getManufacturer().equalsIgnoreCase(lek.getName()))
            errors.rejectValue("name", "NameManufacturerTheSame");
    }
}
