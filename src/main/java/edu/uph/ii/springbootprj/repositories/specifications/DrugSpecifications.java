package edu.uph.ii.springbootprj.repositories.specifications;

import edu.uph.ii.springbootprj.domain.Drug;
import edu.uph.ii.springbootprj.domain.ProductType;
import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.util.StringUtils;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;

public class DrugSpecifications {
    public static Specification<Drug> findByPhrase(final String phrase) {

        return (root, query, cb) -> {
            if(StringUtils.isEmpty(phrase) == false){
                String phraseLike = "%"+phrase.toUpperCase() +"%";
                return cb.or(
                        cb.like(cb.upper(root.get("name")), phraseLike),
                        cb.like(cb.upper(root.get("manufacturer")), phraseLike),
                        cb.like(cb.upper(root.get("productType").get("name")), phraseLike)
                );
            }return null;
        };
    }

    public static Specification<Drug> findByPriceRange(Float minPrice, Float maxPrice) {
        return (root, query, cb) -> {
            if(minPrice != null && maxPrice != null){
                return cb.between(root.get("price"), minPrice, maxPrice);
            }else if(minPrice != null){
                return cb.greaterThanOrEqualTo(root.get("price"),minPrice);
            }else if(maxPrice != null) {
                return cb.lessThanOrEqualTo(root.get("price"),maxPrice);
            }
            return null;
        };
    }

    public static Specification<Drug> findByDateRange(LocalDate dateFrom, LocalDate dateTo) {
        return (root, query, cb) -> {
            if(dateFrom != null && dateTo != null){
                return cb.between(root.get("releaseDate"), dateFrom, dateTo);
            }else if(dateFrom != null){
                return cb.greaterThanOrEqualTo(root.get("releaseDate"),dateFrom);
            }else if(dateTo != null) {
                return cb.lessThanOrEqualTo(root.get("releaseDate"),dateTo);
            }
            return null;
        };
    }

    /*
    public static Specification<Drug> findByProductType(List<ProductType> productTypes) {
        return (root, query, cb) -> {
            if(productTypes != null || !productTypes.isEmpty()){

                cb.inproductTypes.contains(root.get("productType"));
                productTypes.stream().filter(x -> root.get("productType").equals(x)).findAny().orElse(null);
            }
            return null;
        };
    }
    */
}
