package edu.uph.ii.springbootprj.services;

import java.io.IOException;
import java.security.Principal;

import com.lowagie.text.DocumentException;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import edu.uph.ii.springbootprj.domain.Order;

public interface OrderService {
    String parseThymeleafTemplate(Order o);
    void generatePdfFromHtml(String html, String path) throws DocumentException, IOException;
    String showForm(Model model);
    String showCart(Model model, Principal principal);
    String processForm(Model model, Order order, Principal p, BindingResult result) throws IOException;

    String delete(long id, Model model);
    String pay(Model model, long id);
    String addToCart(long id, Model model, Principal p);

    String cancel(Model model, long id);
}
