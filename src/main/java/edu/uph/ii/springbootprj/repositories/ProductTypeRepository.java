package edu.uph.ii.springbootprj.repositories;

import edu.uph.ii.springbootprj.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {

}
