package com.userservice.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OptData {
    private String otp;
    private String phoneNumber;
    private long timestamp;
    private long expiryDuration;

    public boolean isExpired() {
        return System.currentTimeMillis() > (timestamp + expiryDuration);
    }
}
