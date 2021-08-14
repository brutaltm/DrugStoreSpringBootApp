package edu.uph.ii.springbootprj.validators;

import edu.uph.ii.springbootprj.domain.User;
import edu.uph.ii.springbootprj.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.Transient;

public class UsernameExistsValidator implements Validator {
    UserRepository userRepository;

    public UsernameExistsValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(User.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        var user = (User) o;
        if (userRepository.existsUserByUsername(user.getUsername()))
            errors.rejectValue("username", "UsernameTaken");
    }
}
