package edu.uph.ii.springbootprj.services.impl;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import edu.uph.ii.springbootprj.domain.Review;
import edu.uph.ii.springbootprj.repositories.DrugRepository;
import edu.uph.ii.springbootprj.repositories.ReviewRepository;
import edu.uph.ii.springbootprj.repositories.UserRepository;
import edu.uph.ii.springbootprj.services.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DrugRepository drugRepository;

    @Override
    public String processForm(Model model, Review review, Principal p, BindingResult result) {
        //System.out.println("JESTEM TU");
        review.setUser(userRepository.findUserByUsername(p.getName()));
        //review.setDrug(drugRepository.findById(review.getDrug().getId()).get());
        var d = drugRepository.findById(review.getDrug().getId()).get();
        d.updateRating(review.getRating());
        drugRepository.save(d);
        reviewRepository.save(review);
        return "redirect:../drugs/details?id=" + review.getDrug().getId();
    }
}
