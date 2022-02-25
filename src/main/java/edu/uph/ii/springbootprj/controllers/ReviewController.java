package edu.uph.ii.springbootprj.controllers;

import java.io.IOException;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.uph.ii.springbootprj.domain.Review;
import edu.uph.ii.springbootprj.services.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController {

    // Wstrzykiwanie za ponocą konstruktora
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/save")
    public String processForm(Model model, @ModelAttribute("review") @Valid Review review, Principal principal, BindingResult result) throws IOException {
        return reviewService.processForm(model, review, principal, result);
    }
}
