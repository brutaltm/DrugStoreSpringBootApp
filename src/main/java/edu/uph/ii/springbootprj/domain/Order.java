package edu.uph.ii.springbootprj.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="zamowienia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Order {

    @Min(0) @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    @UpdateTimestamp
    @DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime orderedAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderedProduct> zamowione_produkty;

    @NumberFormat(pattern = "#.00")
    private double totalPrice = 0.0;

    private Status status = Status.KOSZYK;

    public enum Status {
        KOSZYK,
        NIEOPLACONY,
        OPLACONY
    }

    public Order(User user, Status status) {
        this.user = user;
        this.status = status;
        zamowione_produkty = new ArrayList<>();
    }

    public boolean isOplacony() {
        return status == Status.OPLACONY;
    }

    @PrePersist @PreUpdate
    public void updatePrice() {
        setOrderedAt(null);
        totalPrice = 0.0;
        getZamowione_produkty().forEach(x -> setTotalPrice(getTotalPrice() + x.getDrug().getPrice() * x.getAmount()));
    }
}
