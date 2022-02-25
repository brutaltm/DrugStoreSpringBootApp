package edu.uph.ii.springbootprj.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import edu.uph.ii.springbootprj.domain.Drug;
import edu.uph.ii.springbootprj.domain.DrugForm;
import edu.uph.ii.springbootprj.domain.ProductType;
import edu.uph.ii.springbootprj.filters.DrugFilter;

public interface DrugRepository extends JpaRepository<Drug, Long>, JpaSpecificationExecutor<Drug> {
    Page<Drug> findDrugsByNameIgnoreCaseContainingOrManufacturerIgnoreCaseContaining(String phrase, String phrase2, Pageable pageable);

    Page<Drug> findDrugsUsingNamedQuery(
            String phrase,
            Float minPrice,
            Float maxPrice,
            List<ProductType> productTypes,
            List<DrugForm> forms,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
            );

    @Query(
            "SELECT d FROM Drug d WHERE " +
                    "(" +
                    ":phrase is null OR :phrase = '' " +
                    "OR upper(d.name) LIKE upper(:phrase) " +
                    "OR upper(d.manufacturer) LIKE upper(:phrase) " +
                    "OR upper(d.productType.name) LIKE upper(:phrase)" +
                    ") " +
                    "AND " +
                    "(:minPrice is null OR :minPrice <= d.price) " +
                    "AND (:maxPrice is null OR :maxPrice >= d.price) " +
                    "AND (COALESCE(:productTypes) is null OR d.productType in :productTypes) " +
                    "AND (COALESCE(:forms) is null OR EXISTS (SELECT f FROM d.forms f WHERE f IN :forms)) " +
                    "AND (:dateFrom is null OR :dateFrom <= d.releaseDate) " +
                    "AND (:dateTo is null OR :dateTo >= d.releaseDate) " +
                    ""
    )
    Page<Drug> findDrugsUsingQuery(
            String phrase,
            Float minPrice,
            Float maxPrice,
            List<ProductType> productTypes,
            List<DrugForm> forms,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    );


    //@Query("SELECT d FROM Drug d")
    //Stream<Drug> findDrugsAndGetStream();

    @EntityGraph(attributePaths = {"productType", "forms"})
    @Query(
            "SELECT d FROM Drug d WHERE " +
                    "(" +
                    ":#{#filter.phraseLIKE} = '' " +
                    "OR upper(d.name) LIKE upper(:#{#filter.phraseLIKE}) " +
                    "OR upper(d.manufacturer) LIKE upper(:#{#filter.phraseLIKE}) " +
                    "OR upper(d.productType.name) LIKE upper(:#{#filter.phraseLIKE})" +
                    ") " +
                    "AND " +
                    "(:#{#filter.minPrice} is null OR :#{#filter.minPrice} <= d.price) " +
                    "AND (:#{#filter.maxPrice} is null OR :#{#filter.maxPrice} >= d.price) " +
                    "AND (:#{#filter.productTypesEmpty} = true OR d.productType in :#{#filter.productTypes}) " +
                    "AND (:#{#filter.formsEmpty} = true OR EXISTS (SELECT f FROM d.forms f WHERE f IN :#{#filter.forms}))" +
                    "AND (:#{#filter.dateFrom} is null OR :#{#filter.dateFrom} <= d.releaseDate) " +
                    "AND (:#{#filter.dateTo} is null OR :#{#filter.dateTo} >= d.releaseDate) " +
                    ""
    )
    Page<Drug> findDrugsUsingSPeL(
            DrugFilter filter,
            Pageable pageable
    );
}
