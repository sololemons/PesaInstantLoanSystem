package com.userservice.security.authentication.services;

import com.userservice.security.dtos.OptData;
import com.userservice.security.dtos.ResetPasswordRequestDto;
import com.userservice.user.entities.User;
import com.userservice.user.exceptions.MissingFieldException;
import com.userservice.user.exceptions.UserNotFoundException;
import com.userservice.user.repositories.UserRepository;
import com.userservice.user.services.SmsService;
import com.userservice.user.utility.OtpStore;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final SmsService smsService;
    private final UserRepository repository;
    private final OtpStore otpStore;
    private final PasswordEncoder passwordEncoder;

    public void sendOtp(String phoneNumber) {


        User user = repository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User with phone number not found"));

        String otp = smsService.generateOtp();

        if (!smsService.sendOtp(phoneNumber, otp)) {
            throw new RuntimeException("Failed to send OTP");
        }
    }
    @Transactional
    public void verifyOtp(String phoneNumber, String submittedOtp) {


        User user = repository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User with phone number not found"));

        OptData otpData = otpStore.getOtp(phoneNumber);

        if (otpData == null) {
            throw new RuntimeException("OTP has expired or was not found");
        }

        if (!otpData.getPhoneNumber().equals(phoneNumber)) {
            throw new RuntimeException("OTP is not associated with this phone number");
        }

        if (otpData.isExpired()) {
            otpStore.removeOtp(phoneNumber);
            throw new RuntimeException("OTP has expired");
        }

        if (!otpData.getOtp().equals(submittedOtp)) {
            throw new RuntimeException("Invalid OTP");
        }
    }
    @Transactional
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {



        if (!resetPasswordRequestDto.getNewPassword().equals(resetPasswordRequestDto.getConfirmNewPassword())) {
            throw new UserNotFoundException("Password should match");
        }

        User user = repository.findByPhoneNumber(resetPasswordRequestDto.getPhoneNumber())
                .orElseThrow(() -> new UserNotFoundException("User with phone number not found"));

        OptData otpData = otpStore.getOtp(resetPasswordRequestDto.getPhoneNumber());

        if (otpData == null) {
            throw new MissingFieldException("OTP has expired or was not found");
        }

        if (!otpData.getPhoneNumber().equals(resetPasswordRequestDto.getPhoneNumber())) {
            throw new MissingFieldException("OTP is not associated with this phone number");
        }

        if (otpData.isExpired()) {
            otpStore.removeOtp(resetPasswordRequestDto.getPhoneNumber());
            throw new MissingFieldException("OTP has expired");
        }

        if (!otpData.getOtp().equals(resetPasswordRequestDto.getOtp())) {
            throw new MissingFieldException("Invalid OTP");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
        repository.save(user);

        otpStore.removeOtp(resetPasswordRequestDto.getPhoneNumber());
    }
}