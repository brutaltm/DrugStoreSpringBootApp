package edu.uph.ii.springbootprj.repositories;

import edu.uph.ii.springbootprj.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser(User user, Pageable pageable);
    Page<Order> findAllByUserAndStatus(User user, Order.Status status, Pageable pageable);
    List<Order> findAllByUserAndStatus(User user, Order.Status status);
    Order findByUserAndStatus(User user, Order.Status status);
    Page<Order> findAllByUserAndStatusOrUserAndStatus(User user, Order.Status status, User user2, Order.Status status2, Pageable pageable);
}
