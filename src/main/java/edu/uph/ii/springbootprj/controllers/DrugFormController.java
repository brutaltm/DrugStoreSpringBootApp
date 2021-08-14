package edu.uph.ii.springbootprj.controllers;

import edu.uph.ii.springbootprj.domain.Drug;
import edu.uph.ii.springbootprj.domain.DrugForm;
import edu.uph.ii.springbootprj.domain.ProductType;
import edu.uph.ii.springbootprj.services.impl.DrugService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/drugs")
@SessionAttributes({"typyProduktow"})
public class DrugFormController {

    // Wstrzykiwanie za ponocą konstruktora
    private DrugService drugService;

    public DrugFormController(DrugService drugService) {
        this.drugService = drugService;
    }

    @GetMapping({"/add", "/edit"})
    public String showForm(Model model, Optional<Long> id) {
        return drugService.showForm(model, id);
    }

    @PostMapping("save")
    public String processForm(Model model, MultipartFile multipartFile, @ModelAttribute("lek") @Valid Drug drug, BindingResult result) throws IOException {
        return drugService.processForm(model,multipartFile,drug,result);
    }

    @InitBinder("lek")
    public void InitBinder(WebDataBinder binder) {
        drugService.InitBinder(binder);
    }

    @ModelAttribute("typyProduktow")
    public List<ProductType> loadProductTypes() {
        return drugService.loadProductTypes();
    }

    @ModelAttribute("formyLeku")
    public List<DrugForm> loadDrugForms() {
        return drugService.loadDrugForms();
    }
}
