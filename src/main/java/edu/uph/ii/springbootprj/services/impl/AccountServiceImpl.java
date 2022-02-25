package edu.uph.ii.springbootprj.services.impl;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import edu.uph.ii.springbootprj.domain.Order;
import edu.uph.ii.springbootprj.domain.User;
import edu.uph.ii.springbootprj.repositories.OrderRepository;
import edu.uph.ii.springbootprj.repositories.UserRepository;
import edu.uph.ii.springbootprj.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String showList(Model model, Principal principal, Order.Status status, Pageable pageable) {
        System.out.println(model.getAttribute("user"));
        var page = orderRepository.findAllByUserAndStatus(userRepository.findUserByUsername(principal.getName()), status, pageable);
        //Page page = orderRepository.findAll(pageable);

        model.addAttribute("page", page);
        return "account/orderList";
    }

    @Override
    public String showList(Model model, Principal principal, Order.Status status, Order.Status status2, Pageable pageable) {
        System.out.println(model.getAttribute("user"));
        var page = orderRepository.findAllByUserAndStatusOrUserAndStatus(userRepository.findUserByUsername(principal.getName()), status, userRepository.findUserByUsername(principal.getName()), status2, pageable);
        //Page page = orderRepository.findAll(pageable);

        model.addAttribute("page", page);
        return "account/orderList";
    }

    @Override
    public String showForm(Model model, Principal p) {
        User u = userRepository.findUserByUsername(p.getName());
        model.addAttribute("user", u);
        model.addAttribute("formValidated", false);
        return "account/userForm";
    }

    public String processForm(Model model, @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request, Principal p) {
        if (result.hasErrors()) {
            model.addAttribute("formValidated", true);
            return "account/userForm";
        }
        var u = userRepository.findUserByUsername(p.getName());
        u.setFirstName(user.getFirstName());
        u.setSurName(user.getSurName());
        u.setAddress(user.getAddress());
        u.setZipCode(user.getZipCode());
        u.setPhone(user.getPhone());
        //user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(u);
        return "account/userForm";
    }

}
