package com.round3.realestate.repository;

import com.round3.realestate.entity.EmploymentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmploymentDataRepository extends JpaRepository<EmploymentData, Long> {

    Optional<EmploymentData> findByUserId(Long id);

}
