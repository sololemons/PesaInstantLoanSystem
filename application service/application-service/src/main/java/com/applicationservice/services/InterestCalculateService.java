package com.applicationservice.services;

import com.applicationservice.dtos.PlanDto;
import com.applicationservice.dtos.InterestPlanDto;
import com.applicationservice.dtos.Progress;
import com.applicationservice.entities.ApplicationForm;
import com.applicationservice.entities.InterestPlan;
import com.applicationservice.exceptions.MissingFieldException;
import com.applicationservice.exceptions.UserNotFoundException;
import com.applicationservice.repositories.ApplicationRepository;
import com.applicationservice.repositories.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterestCalculateService {
    private final RepaymentRepository repaymentRepository;
    private final ApplicationRepository applicationRepository;

    public PlanDto calculateInterestPlan(InterestPlanDto interestPlanDto) {
        double annualRate = 0.12;
        double monthlyRate = annualRate / 12;
        double principal = interestPlanDto.getPrincipal();
        double months = interestPlanDto.getMonths();

        if (months <= 0) {
            throw new IllegalArgumentException("Months must be greater than 0.");
        }
        String phoneNumber = interestPlanDto.getPhoneNumber();
        double monthlyInstallment = (principal * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -months));
        double totalRepayableInstallments = monthlyInstallment * months;
        LocalDate repaymentDate = LocalDate.now().plusMonths(2);


        InterestPlan interestPlan = new InterestPlan();
        interestPlan.setPrincipal(principal);
        interestPlan.setInterestRate(annualRate);
        interestPlan.setRepaymentDate(repaymentDate);
        interestPlan.setMonthlyInstallments(monthlyInstallment);
        interestPlan.setTotalPayable(totalRepayableInstallments);
        repaymentRepository.save(interestPlan);

        Optional<ApplicationForm> applicationForm = applicationRepository.findByPhoneNumberAndProgress(phoneNumber,Progress.ACTIVE);

        if (applicationForm.isPresent()) {
            ApplicationForm applicationFormDto = applicationForm.get();
            applicationFormDto.setInterestPlan(interestPlan);
            applicationRepository.save(applicationFormDto);

        }
        else {
            throw new UserNotFoundException("No application found with PhoneNumber " + phoneNumber);
        }

        PlanDto planDto = new PlanDto();
        planDto.setInterestRate(annualRate);
        planDto.setMonthlyInstallments(monthlyInstallment);
        planDto.setTotalRepayableAmount(totalRepayableInstallments);
        planDto.setRepaymentDate(repaymentDate);
        return planDto;

    }


    public PlanDto getInterestPlan(String phoneNumber) {
        return applicationRepository.findByPhoneNumberAndProgress(phoneNumber, Progress.ACTIVE)
                .map(ApplicationForm::getInterestPlan)
                .map(plan -> {
                    PlanDto dto = new PlanDto();
                   dto.setInterestRate(plan.getInterestRate());
                   dto.setMonthlyInstallments(plan.getMonthlyInstallments());
                   dto.setRepaymentDate(plan.getRepaymentDate());
                   dto.setTotalRepayableAmount(plan.getTotalPayable());
                    return dto;
                })
                .orElseThrow(() -> new MissingFieldException("Interest plan not found for phone number: " + phoneNumber));
    }

}
