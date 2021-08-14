package edu.uph.ii.springbootprj.controllers;

import edu.uph.ii.springbootprj.domain.DrugForm;
import edu.uph.ii.springbootprj.domain.ProductType;
import edu.uph.ii.springbootprj.filters.DrugFilter;
import edu.uph.ii.springbootprj.services.impl.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.OperationNotSupportedException;
import java.util.List;

@Controller
@RequestMapping("/drugs")
@SessionAttributes("drugFilter")
public class DrugListController {

    //Wstrzykiwanie z wykorzystaniem pola
    @Autowired
    private DrugService drugService;

    @GetMapping("/details")
    public String showDetails(Model model, long id, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        return drugService.showDetails(model,id,pageable);
    }
    @ModelAttribute
    public DrugFilter loadEmptyFilter() {
        return drugService.loadEmptyFilter();
    }

    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public String showList(Model model,
                           @ModelAttribute("drugFilter") DrugFilter filter,
                           @PageableDefault(sort = {"name", "id"}, size = 20)
                           Pageable pageable) throws OperationNotSupportedException {// page=x&size=y&sort=prop1,ASC&sort=prop2,DESC

        return drugService.showList(model,filter,pageable);
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, Model model, @ModelAttribute("drugFilter") DrugFilter filter,
                         @PageableDefault(sort = {"name", "id"}, size = 20)
                         Pageable pageable) {

        return drugService.delete(id,model,filter,pageable);
    }

    @PostMapping(value="list", params = {"clear"})
    public String clearFilter(@ModelAttribute("drugFilter") DrugFilter filter) {
        return drugService.clearFilter(filter);
    }

    @ModelAttribute("typyProduktow")
    public List<ProductType> loadProductTypes() {
        return drugService.loadProductTypes();
    }

    @ModelAttribute("formyLeku")
    public List<DrugForm> loadDrugForms() {
        return drugService.loadDrugForms();
    }

    @ModelAttribute("typyZapytan")
    public DrugFilter.QUERY_MODE[] loadQueryModes() {
            return drugService.loadQueryModes();
    }
}
