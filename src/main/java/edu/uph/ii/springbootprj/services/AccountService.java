package edu.uph.ii.springbootprj.services;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import edu.uph.ii.springbootprj.domain.Order;
import edu.uph.ii.springbootprj.domain.User;

public interface AccountService {

    String showList(Model model, Principal principal, Order.Status status, Pageable pageable);
    String showList(Model model, Principal principal, Order.Status status, Order.Status status2, Pageable pageable);
    String showForm(Model model, Principal p);

    String processForm(Model model, User user, BindingResult result, HttpServletRequest request, Principal p);
}
