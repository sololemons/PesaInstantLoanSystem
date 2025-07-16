package com.verification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdDetailsPayload {
    private long userId;
    private String idNumber;
}
