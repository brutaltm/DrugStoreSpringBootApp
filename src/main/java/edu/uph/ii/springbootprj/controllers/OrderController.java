package edu.uph.ii.springbootprj.controllers;

import java.io.IOException;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.uph.ii.springbootprj.domain.Order;
import edu.uph.ii.springbootprj.services.OrderService;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/cart")
    public String showCart(Model model, Principal principal) {
        return orderService.showCart(model, principal);
}

    @RequestMapping("/cart/save")
    public String processForm(Model model, @ModelAttribute("order") @Valid Order order, Principal p, BindingResult result) throws IOException {
        return orderService.processForm(model,order,p,result);
    }

    @RequestMapping("/cart/add/{id}")
    public String addToCart(@PathVariable long id, Model model, Principal principal) {
        return orderService.addToCart(id,model,principal);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, Model model) {
        return orderService.delete(id, model);
    }

}
