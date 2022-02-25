package edu.uph.ii.springbootprj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uph.ii.springbootprj.domain.DrugForm;

public interface DrugFormRepository extends JpaRepository<DrugForm, Integer> {

}
