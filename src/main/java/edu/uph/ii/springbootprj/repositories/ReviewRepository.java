package edu.uph.ii.springbootprj.repositories;

import edu.uph.ii.springbootprj.domain.Drug;
import edu.uph.ii.springbootprj.domain.Order;
import edu.uph.ii.springbootprj.domain.Review;
import edu.uph.ii.springbootprj.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByUser(User user, Pageable pageable);
    Page<Review> findAllByDrug(Drug drug, Pageable pageable);
}
