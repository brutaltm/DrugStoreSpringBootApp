package edu.uph.ii.springbootprj.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import edu.uph.ii.springbootprj.validators.IllegalManufacturerNames;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "leki")
@Getter @Setter
@NamedQuery(
        name = "Drug.findDrugsUsingNamedQuery",
        query = "SELECT d FROM Drug d WHERE " +
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
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @NotEmpty @Size(min = 2, max = 50)
    private String name="";
    @NotBlank @NotEmpty @Size(min = 2, max = 50)//, message = "Zakres od {min} do {max} znaków")
    @IllegalManufacturerNames(values = {"Producent"}, ignoreCase = false)
    private String manufacturer="";
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent
    private LocalDate releaseDate;
    @NumberFormat(pattern = "#.00") @Min(0) @Max(1000) @NotNull
    private Float price;
    @NumberFormat(pattern = "#.00") @Min(0) @Max(1000)
    private Float promoprice;
    private boolean discount;
    @Transient
    private Date createdDate;
    @Valid
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private ProductType productType;

    @ManyToMany
    //@NotEmpty
    private Set<DrugForm> forms;

    @OneToMany(mappedBy = "drug", fetch = FetchType.EAGER)
    private List<Review> reviews;

    private double rating;

    public void updateRating(byte r) {
        rating = (rating*reviews.size()+r)/(reviews.size()+1);
    }

    public double getRating() {
        double sum = 0;
        for (var r : reviews)
            sum += r.getRating();

        return reviews.size() == 0 ? 0.0 : sum/reviews.size();
    }

    private PhotoDesc photo = new PhotoDesc();

    public Drug() {
        this.releaseDate = LocalDate.now();
        this.createdDate = new Date();
        forms = new HashSet<>();
        reviews = new ArrayList<>();
    }

    public Drug(Long id, String name, String manufacturer, LocalDate releaseDate, Float price, Float promoprice, boolean discount) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.releaseDate = releaseDate;
        this.price = price;
        this.promoprice = promoprice;
        this.discount = discount;
        this.createdDate = new Date();
        forms = new HashSet<>();
    }
}
