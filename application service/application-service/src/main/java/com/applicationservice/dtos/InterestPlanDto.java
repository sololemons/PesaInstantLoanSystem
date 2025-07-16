package com.applicationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestPlanDto {
    private double months;
    private String interestRate;
    private double principal;
    private String phoneNumber;

}
