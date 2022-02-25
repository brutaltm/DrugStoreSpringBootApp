package edu.uph.ii.springbootprj.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uph.ii.springbootprj.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);
    boolean existsUserByUsername(String username);
    Optional<User> findUserByActivationCodeAndEnabledFalse(String code);
}
