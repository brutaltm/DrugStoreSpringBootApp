package edu.uph.ii.springbootprj.repositories;

import edu.uph.ii.springbootprj.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);
    boolean existsUserByUsername(String username);
    Optional<User> findUserByActivationCodeAndEnabledFalse(String code);
}
