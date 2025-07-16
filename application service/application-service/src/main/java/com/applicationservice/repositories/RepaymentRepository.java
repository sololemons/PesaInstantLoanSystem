package com.applicationservice.repositories;

import com.applicationservice.entities.InterestPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepaymentRepository extends JpaRepository<InterestPlan, Long> {
}
