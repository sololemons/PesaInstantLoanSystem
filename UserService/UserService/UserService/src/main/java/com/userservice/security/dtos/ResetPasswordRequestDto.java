package com.userservice.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequestDto {
    private String phoneNumber;
    private String otp;
    private String newPassword;
    private String confirmNewPassword;
}
