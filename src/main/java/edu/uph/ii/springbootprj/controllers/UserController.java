package edu.uph.ii.springbootprj.controllers;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import edu.uph.ii.springbootprj.domain.User;
import edu.uph.ii.springbootprj.exceptions.UserNotFoundOrAlreadyActivatedException;
import edu.uph.ii.springbootprj.repositories.UserRepository;
import edu.uph.ii.springbootprj.services.UserService;
import edu.uph.ii.springbootprj.validators.MustBeTheSameValidator;
import edu.uph.ii.springbootprj.validators.UsernameExistsValidator;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    public UserController(UserService userService){this.userService = userService;}

    @GetMapping("register")
    public String showForm(Model model, Optional<Long> id) {
        model.addAttribute("user", new User());
        model.addAttribute("formValidated", false);
        return "registerForm";
    }

    @PostMapping("saveUser")
    public String processForm(Model model, @ModelAttribute("user") @Valid User user, BindingResult result, HttpServletRequest request) throws MessagingException {

        if (result.hasErrors()) {
            model.addAttribute("formValidated", true);
            return "registerForm";
        }
        //user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.saveUser(user,getSiteURL(request));
        return "registerSuccess";
    }

    @GetMapping("activate")
    public String activateAccount(Model model, String code) {
        User user = userRepository.findUserByActivationCodeAndEnabledFalse(code).orElseThrow(() -> new UserNotFoundOrAlreadyActivatedException(code));
        user.setEnabled(true);
        userRepository.save(user);
        model.addAttribute("user",user);
        return "activationSuccess";
    }

    @InitBinder("user")
    public void InitBinder(WebDataBinder binder) {
        binder.addValidators(new MustBeTheSameValidator());
        binder.addValidators(new UsernameExistsValidator(userRepository));
        binder.setDisallowedFields("enabled");
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
