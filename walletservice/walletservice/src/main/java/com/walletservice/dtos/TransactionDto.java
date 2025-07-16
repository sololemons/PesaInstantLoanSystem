package com.walletservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String transactionAmount;
    private String phoneNumber;
    private String transactionDate;
    private String transactionType;
    private String transactionCode;
}
