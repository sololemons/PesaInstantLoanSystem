package com.verification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrbResult {
    private int age;
    private int noOfLoans;
    private int noOfPaidLoans;
    private int noOfUnpaidLoans;
    private int noOfPaidUnpaidLoansPastDue;
    private boolean crbStatus;
    private long userId;
}
