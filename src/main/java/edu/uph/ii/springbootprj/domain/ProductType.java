package edu.uph.ii.springbootprj.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
@Entity
@Table(name="typy_produktow")
@Getter @Setter @NoArgsConstructor
public class ProductType {
    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="typ_produktu")
    private String name;

    public ProductType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
             return true;
        if (obj == null)
             return false;
        ProductType df = (ProductType)obj;
        if (df.id == this.id && df.name == this.name)
            return true;
        return false;
    }
}
