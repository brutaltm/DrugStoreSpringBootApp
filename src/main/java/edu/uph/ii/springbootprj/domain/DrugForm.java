package edu.uph.ii.springbootprj.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "forms")
@Data
public class DrugForm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToMany(mappedBy = "forms", fetch = FetchType.EAGER)
    private Set<Drug> drugs;

    public DrugForm() {
        drugs = new HashSet<>();
    }

    public DrugForm(String name) {
        this.name = name;
        drugs = new HashSet<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
             return true;
        if (obj == null)
             return false;
        DrugForm df = (DrugForm)obj;
        if (df.id == this.id && df.name == this.name)
            return true;
        return false;
    }
}
