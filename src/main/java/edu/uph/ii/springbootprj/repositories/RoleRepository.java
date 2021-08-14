package edu.uph.ii.springbootprj.repositories;

import edu.uph.ii.springbootprj.domain.Role;
import edu.uph.ii.springbootprj.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByType(Role.Types role);
}
