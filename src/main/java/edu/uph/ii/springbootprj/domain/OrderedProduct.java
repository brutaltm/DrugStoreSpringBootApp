package edu.uph.ii.springbootprj.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Set;

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
