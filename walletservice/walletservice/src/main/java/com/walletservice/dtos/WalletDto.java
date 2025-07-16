package com.walletservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {
    private String phoneNumber;
    private String walletBalance;
    private String createdAt;
    private String fundsDue;
}
