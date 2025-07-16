package com.applicationservice.repositories;

import com.applicationservice.dtos.Progress;
import com.applicationservice.entities.ApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationForm, Integer> {
    List<ApplicationForm> findByUserId(String userId);
    Optional<ApplicationForm> findByUserIdAndProgress(String userId, Progress progress);
    Optional<ApplicationForm> findByPhoneNumberAndProgress(String phoneNumber, Progress progress);

}
