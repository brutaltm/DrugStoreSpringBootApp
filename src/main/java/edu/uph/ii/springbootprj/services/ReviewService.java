package edu.uph.ii.springbootprj.services;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import edu.uph.ii.springbootprj.domain.Review;

public interface ReviewService {
    String processForm(Model model, Review review, Principal p, BindingResult result);
}
