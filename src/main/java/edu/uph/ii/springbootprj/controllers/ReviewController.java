package edu.uph.ii.springbootprj.controllers;

import edu.uph.ii.springbootprj.domain.Drug;
import edu.uph.ii.springbootprj.domain.DrugForm;
import edu.uph.ii.springbootprj.domain.ProductType;
import edu.uph.ii.springbootprj.domain.Review;
import edu.uph.ii.springbootprj.services.ReviewService;
import edu.uph.ii.springbootprj.services.impl.DrugService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
