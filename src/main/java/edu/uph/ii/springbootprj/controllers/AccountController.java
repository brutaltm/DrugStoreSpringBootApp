package edu.uph.ii.springbootprj.controllers;

import edu.uph.ii.springbootprj.domain.Order;
import edu.uph.ii.springbootprj.domain.User;
import edu.uph.ii.springbootprj.services.AccountService;
import edu.uph.ii.springbootprj.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@Secured({"ROLE_USER", "ROLE_ADMIN"})
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderService orderService;

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value="/orders", method = {RequestMethod.GET, RequestMethod.POST})
    public String showList(Model model, Principal principal, @PageableDefault(sort = "orderedAt", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        return accountService.showList(model, principal, Order.Status.OPLACONY, Order.Status.NIEOPLACONY, pageable);
    }

    @GetMapping("/orders/pay")
    public String pay(Model model, long id) {
        return orderService.pay(model,id);
    }

    @GetMapping("/orders/cancel")
    public String cancel(Model model, long id) {
        return orderService.cancel(model,id);
    }

    @GetMapping("/data")
    public String showForm(Model model, Principal p) { return accountService.showForm(model,p); }

    @PostMapping("/save")
    public String processForm(Model model, @ModelAttribute("user") @Valid User user, BindingResult result, HttpServletRequest request, Principal p) {
        return accountService.processForm(model,user,result,request,p);
    }
}
