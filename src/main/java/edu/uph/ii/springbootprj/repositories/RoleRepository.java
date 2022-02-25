package edu.uph.ii.springbootprj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uph.ii.springbootprj.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByType(Role.Types role);
}
