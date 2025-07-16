package com.userservice.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPasswordRequestDto {
    private String phoneNumber;
    private String otp;
}
