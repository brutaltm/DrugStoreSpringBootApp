package edu.uph.ii.springbootprj.services.impl;

import edu.uph.ii.springbootprj.domain.*;
import edu.uph.ii.springbootprj.exceptions.DrugNotFoundException;
import edu.uph.ii.springbootprj.filters.DrugFilter;
import edu.uph.ii.springbootprj.repositories.DrugFormRepository;
import edu.uph.ii.springbootprj.repositories.DrugRepository;
import edu.uph.ii.springbootprj.repositories.ProductTypeRepository;
import edu.uph.ii.springbootprj.repositories.ReviewRepository;
import edu.uph.ii.springbootprj.repositories.specifications.DrugSpecifications;
import edu.uph.ii.springbootprj.services.PhotoService;
import edu.uph.ii.springbootprj.validators.TheSameValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DrugService {
    // Wstrzykiwanie za ponocą konstruktora
    private DrugRepository drugRepository;
    private ProductTypeRepository productTypeRepository;
    private DrugFormRepository drugFormRepository;
    private PhotoService photoService;
    private ReviewRepository reviewRepository;

    public DrugService(DrugRepository drugRepository, ProductTypeRepository productTypeRepository,
                       DrugFormRepository drugFormRepository, PhotoService photoService,
                       ReviewRepository reviewRepository) {
        this.drugRepository = drugRepository;
        this.productTypeRepository = productTypeRepository;
        this.drugFormRepository = drugFormRepository;
        this.photoService = photoService;
        this.reviewRepository = reviewRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String showForm(Model model, Optional<Long> id) {
        if (id.isPresent() == false) { //dodawanie nowego
            model.addAttribute("lek", new Drug());
        } else {
            model.addAttribute("lek", drugRepository.findById(id.get()).orElseThrow(() -> new DrugNotFoundException(id.get())));
        }
        model.addAttribute("formValidated", false);
        return "drugs/drugForm";
    }

    @Secured("ROLE_ADMIN")
    public String processForm(Model model, MultipartFile multipartFile, Drug drug, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("formValidated", true);
            return "drugs/drugForm";
        }

        if (!multipartFile.isEmpty()) {
            var photo = new PhotoDesc();
            photo.setFileName(multipartFile.getOriginalFilename());
            photo.setContent(multipartFile.getBytes());
            drug.setPhoto(photo);
            photoService.saveFile(String.format("drugs/%d",drug.getId()),multipartFile);
            photoService.saveFileWithResizing(String.format("drugs/%d",drug.getId()), multipartFile);
        }
        drugRepository.save(drug);
        return "redirect:/drugs/details?id=" + drug.getId();
    }

    public void InitBinder(WebDataBinder binder) {
        binder.addValidators(new TheSameValidator());
    }

    public List<ProductType> loadProductTypes() {
        return productTypeRepository.findAll();
    }

    public List<DrugForm> loadDrugForms() { return drugFormRepository.findAll(); }

    // Drug List

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public String showDetails(Model model, long id, Pageable pageable) {
        var lek = drugRepository.findById(id).orElseThrow(() -> new DrugNotFoundException(id));
        model.addAttribute("lek", lek);
        var a = new Review();
        a.setDrug(lek);
        model.addAttribute("review", a);
        var page = reviewRepository.findAllByDrug(lek, pageable);
        model.addAttribute("page", page);
        System.out.println("ID: " + id);
        return "drugs/drugDetails";
    }

    public DrugFilter loadEmptyFilter() {
        return new DrugFilter();
    }

    public String showList(Model model,
                           DrugFilter filter,
                           Pageable pageable) throws OperationNotSupportedException {// page=x&size=y&sort=prop1,ASC&sort=prop2,DESC

        Page<Drug> page = null;

        if (filter.isEmpty())
            page = drugRepository.findAll(pageable);
        else {
            switch (filter.getQueryMode()) {
                case NAMED_METHOD:
                    page = drugRepository.findDrugsByNameIgnoreCaseContainingOrManufacturerIgnoreCaseContaining
                            (filter.getPhrase(), filter.getPhrase(), pageable);
                    break;
                case NAMED_QUERY:
                    page = drugRepository.findDrugsUsingNamedQuery(
                            filter.getPhraseLIKE(),
                            filter.getMinPrice(),
                            filter.getMaxPrice(),
                            filter.isProductTypesEmpty()?null:filter.getProductTypes(),
                            filter.isFormsEmpty()?null:filter.getForms(),
                            filter.getDateFrom(),
                            filter.getDateTo(),
                            pageable
                    );
                    break;
                case QUERY:
                    page = drugRepository.findDrugsUsingQuery(
                            filter.getPhraseLIKE(),
                            filter.getMinPrice(),
                            filter.getMaxPrice(),
                            filter.isProductTypesEmpty()?null:filter.getProductTypes(),
                            filter.isFormsEmpty()?null:filter.getForms(),
                            filter.getDateFrom(),
                            filter.getDateTo(),
                            pageable
                    );
                    break;
                case SpEL_AND_ENTITY_GRAPH:
                    page = drugRepository.findDrugsUsingSPeL(filter, pageable);
                    break;
                case CRITERIA:
                    page = drugRepository.findAll(
                            Specification.where(
                                    DrugSpecifications.findByPhrase(filter.getPhrase()).
                                            and(
                                                    DrugSpecifications.findByPriceRange(filter.getMinPrice(), filter.getMaxPrice())
                                            ).and(
                                            DrugSpecifications.findByDateRange(filter.getDateFrom(), filter.getDateTo())
                                    )
                            ), pageable
                    );
                    break;
                default:
                    throw new OperationNotSupportedException(String.format("Typ '%s' nie jest obsługiwany", filter.getQueryMode()));
            }
        }
        model.addAttribute("page", page);
        return "drugs/drugList";
    }

    @Secured("ROLE_ADMIN")
    public String delete(long id, Model model,DrugFilter filter, Pageable pageable) {
        drugRepository.deleteById(id);
        return "redirect:./list";
    }

    public String clearFilter(DrugFilter filter) {
        filter.clear();
        return "redirect:list";
    }

    public DrugFilter.QUERY_MODE[] loadQueryModes() {
        return DrugFilter.QUERY_MODE.values();
    }

}
