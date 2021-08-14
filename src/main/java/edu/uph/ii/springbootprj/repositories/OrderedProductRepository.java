package edu.uph.ii.springbootprj.repositories;

import edu.uph.ii.springbootprj.domain.DrugForm;
import edu.uph.ii.springbootprj.domain.Order;
import edu.uph.ii.springbootprj.domain.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {

    List<OrderedProduct> findAllByOrder(Order order);
}
