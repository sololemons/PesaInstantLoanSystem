package com.applicationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDto {
    private double interestRate;
    private LocalDate repaymentDate;
    private double monthlyInstallments;
    private double totalRepayableAmount;
}
