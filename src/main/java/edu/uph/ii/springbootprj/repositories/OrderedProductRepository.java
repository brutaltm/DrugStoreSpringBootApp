package edu.uph.ii.springbootprj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uph.ii.springbootprj.domain.Order;
import edu.uph.ii.springbootprj.domain.OrderedProduct;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {

    List<OrderedProduct> findAllByOrder(Order order);
}
