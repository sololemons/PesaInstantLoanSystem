package com.userservice.user.utility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.security.dtos.OptData;
import com.userservice.user.exceptions.MissingFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OtpStore {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void storeOtp(String phoneNumber, OptData otpData) {
        try {
            String redisKey = "otp:" + phoneNumber;
            String otpDataJson = objectMapper.writeValueAsString(otpData);
            redisTemplate.opsForValue().set(redisKey, otpDataJson, 10, TimeUnit.MINUTES);
            System.out.println("Stored OTP in Redis for: " + phoneNumber);
        } catch (JsonProcessingException e) {
            throw new MissingFieldException("Failed to serialize OTP data: " + e.getMessage());
        } catch (Exception e) {
            throw new MissingFieldException("Failed to store OTP in Redis: " + e.getMessage());
        }
    }

    public OptData getOtp(String phoneNumber) {
        try {
            String redisKey = "otp:" + phoneNumber;
            String otpDataJson = redisTemplate.opsForValue().get(redisKey);
            if (otpDataJson == null) {
                return null;
            }
            return objectMapper.readValue(otpDataJson, OptData.class);
        } catch (JsonProcessingException e) {
            throw new MissingFieldException("Failed to deserialize OTP data: " + e.getMessage());
        } catch (Exception e) {
            throw new MissingFieldException("Failed to retrieve OTP from Redis: " + e.getMessage());
        }
    }

    public void removeOtp(String phoneNumber) {
        try {
            String redisKey = "otp:" + phoneNumber;
            redisTemplate.delete(redisKey);
            System.out.println("Removed OTP from Redis for: " + phoneNumber);
        } catch (Exception e) {
            throw new MissingFieldException("Failed to remove OTP from Redis: " + e.getMessage());
        }
    }
}