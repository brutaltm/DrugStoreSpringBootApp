package edu.uph.ii.springbootprj.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Zamowione_produkty")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderedProduct {

    @Min(0) @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    @ManyToOne
    private Drug drug;

    @Column(name="ilosc")
    private Integer amount;

    //@PostPersist
    //private void asd() {
    //    order.setTotalPrice(order.getTotalPrice() + drug.getPrice() * amount);
    //}
}
