package edu.uph.ii.springbootprj.domain;

import edu.uph.ii.springbootprj.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.hibernate.mapping.FetchProfile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, max = 36)
    private String username;
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$")
    //@Size(min = 2, max = 36)
    private String password;
    @Transient//właściwość nie będzie odzwierciedlona w db
    private String passwordConfirm;
    private String email;
    private String firstName="";
    private String surName="";
    private String phone="";
    private String address="";
    private String zipCode="";
    private String activationCode="";
    private boolean enabled = false;//czy konto jest aktywne

    @AssertTrue
    private boolean arePasswordsTheSame() {
        return password == null || passwordConfirm == null || password.equals(passwordConfirm);
    }

    public boolean dataFilled() {
        return !(firstName.isEmpty() || firstName.isBlank() || surName.isEmpty() || surName.isBlank() || phone.isEmpty()
                || phone.isBlank() || address.isEmpty() || address.isBlank() || zipCode.isEmpty() || zipCode.isBlank());
    }

    @ManyToMany//(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String username){
        this(username, false);
    }

    public User(String username, boolean enabled){
        this.username = username;
        this.enabled = enabled;
    }

    /*Bezargumentowy konstruktor gettery i setery lub Lombok */
}
