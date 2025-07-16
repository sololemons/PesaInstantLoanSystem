package com.userservice.security.authentication.controllers;
import com.userservice.security.authentication.services.ResetPasswordService;
import com.userservice.security.dtos.ForgotPasswordDto;
import com.userservice.security.dtos.ResetPasswordRequestDto;
import com.userservice.security.dtos.VerifyPasswordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/forgot")
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        String phoneNumber = forgotPasswordDto.getPhoneNumber();
        resetPasswordService.sendOtp(phoneNumber);
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyPasswordRequestDto verifyOtpDto) {
        resetPasswordService.verifyOtp(verifyOtpDto.getPhoneNumber(), verifyOtpDto.getOtp());
        return ResponseEntity.ok("OTP verified successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDto resetPasswordDto) {
        resetPasswordService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok("Password reset successfully");
    }
}