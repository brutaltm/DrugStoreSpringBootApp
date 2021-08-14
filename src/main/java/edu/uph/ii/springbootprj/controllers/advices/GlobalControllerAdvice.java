package edu.uph.ii.springbootprj.controllers.advices;

import edu.uph.ii.springbootprj.controllers.DrugFormController;
import edu.uph.ii.springbootprj.controllers.DrugListController;
import edu.uph.ii.springbootprj.controllers.UserController;
import edu.uph.ii.springbootprj.exceptions.DrugNotFoundException;
import edu.uph.ii.springbootprj.exceptions.OrderNotFoundOrConfirmedException;
import edu.uph.ii.springbootprj.exceptions.UserNotFoundOrAlreadyActivatedException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackageClasses = {DrugFormController.class, DrugListController.class, UserController.class})
@Log4j2
public class GlobalControllerAdvice {

    @ExceptionHandler(DrugNotFoundException.class)
    public String handleDrugNotFoundError(Model model, HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + " raised " + ex);

        model.addAttribute("exception", ex);
        model.addAttribute("url", req.getRequestURL());

        return "errors/error404drugNotFound";
    }

    @ExceptionHandler({JDBCConnectionException.class, DataIntegrityViolationException.class})
    public String handleDbError(Model model, HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + " raised " + ex);

        model.addAttribute("exception", ex);
        model.addAttribute("url", req.getRequestURL());

        return "errors/databaseError";
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public String handleUserNotFoundError(Model model, HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + " raised " + ex);

        model.addAttribute("exception", ex);
        model.addAttribute("url", req.getRequestURL());

        return "errors/error404UserNotFound";
    }

    @ExceptionHandler(UserNotFoundOrAlreadyActivatedException.class)
    public String handleUserNotFoundOrActivatedError(Model model, HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + " raised " + ex);

        model.addAttribute("exception", ex);
        model.addAttribute("url", req.getRequestURL());

        return "errors/error404UserNotFound";
    }

    @ExceptionHandler(OrderNotFoundOrConfirmedException.class)
    public String handleOrderNotFoundOrConfirmed(Model model, HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + " raised " + ex);

        model.addAttribute("exception", ex);
        model.addAttribute("url", req.getRequestURL());

        return "errors/error404OrderNotFound";
    }
}
