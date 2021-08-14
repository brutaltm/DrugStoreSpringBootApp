package edu.uph.ii.springbootprj.services;

import edu.uph.ii.springbootprj.domain.Review;
import edu.uph.ii.springbootprj.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import java.security.Principal;

public interface ReviewService {
    String processForm(Model model, Review review, Principal p, BindingResult result);
}
