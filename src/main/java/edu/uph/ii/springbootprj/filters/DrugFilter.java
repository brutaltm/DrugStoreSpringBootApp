package edu.uph.ii.springbootprj.filters;

import edu.uph.ii.springbootprj.domain.DrugForm;
import edu.uph.ii.springbootprj.domain.ProductType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class DrugFilter {
    private String phrase;
    @NumberFormat(pattern = "#.00")
    private Float minPrice;
    @NumberFormat(pattern = "#.00")
    private Float maxPrice;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;
    private List<ProductType> productTypes;
    private List<DrugForm> forms;
    private QUERY_MODE queryMode;

    public DrugFilter() {
        productTypes = new ArrayList<>();
        forms = new ArrayList<>();
    }

    public boolean isEmpty() {
        return (phrase == null || StringUtils.isEmptyOrWhitespace(phrase)) && maxPrice == null && minPrice == null
                && dateFrom == null && dateTo == null && (productTypes == null || productTypes.isEmpty())
                && (forms == null || forms.isEmpty());
    }

    public String getPhraseLIKE() { return phrase.isEmpty()?"":"%"+phrase+"%";}
    public boolean isProductTypesEmpty() { return productTypes.isEmpty(); }
    public boolean isFormsEmpty() { return forms.isEmpty(); }

    public void clear() {
        phrase = null;
        minPrice = null;
        maxPrice = null;
        dateFrom = null;
        dateTo = null;
        productTypes = null;
        forms = null;
    }

    public enum QUERY_MODE {
        QUERY,
        NAMED_METHOD,
        NAMED_QUERY,
        SpEL_AND_ENTITY_GRAPH,
        CRITERIA,
    }
}
