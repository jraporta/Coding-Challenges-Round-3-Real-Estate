package com.round3.realestate.repository;

import com.round3.realestate.entity.Mortgage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MortgageRepository extends JpaRepository<Mortgage, Long> {

    List<Mortgage> findAllByUserId(Long id);

}
