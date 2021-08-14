package edu.uph.ii.springbootprj.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor @Getter @Setter
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)//przechowywane w postaci łańcucha znaków
    private Types type;
    @ManyToMany(mappedBy = "roles")//właściciel relacji to roles
    private Set<User> users;

    public Role(Types type){
        this.type = type;
    }

    public static enum Types{
        ROLE_ADMIN,
        ROLE_USER
    }
    /*Bezargumentowy konstruktor gettery i setery lub Lombok */
}
