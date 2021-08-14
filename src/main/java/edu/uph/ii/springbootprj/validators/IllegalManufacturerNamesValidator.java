package edu.uph.ii.springbootprj.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IllegalManufacturerNamesValidator implements ConstraintValidator<IllegalManufacturerNames, String> {

    public IllegalManufacturerNames constraint;

    public void initialize(IllegalManufacturerNames constraint) {
        this.constraint = constraint;
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {

        if (login != null) {
            if (constraint.ignoreCase() == false) {
                if (!login.equals(login.toUpperCase()) || login.matches("[0-9]+")) {
                    for (var value:constraint.values()) {
                        if (value.equals(login))
                            return false;
                    }
                    return true;
                }
            } else {
                for (var value:constraint.values()) {
                    if (value.equalsIgnoreCase(login))
                        return false;
                }
                return true;
            }
        }

        return false;
    }
}