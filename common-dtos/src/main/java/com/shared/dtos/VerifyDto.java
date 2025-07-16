package com.shared.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyDto{
    private String fileName;
    private long userId;
    private byte[] image;
}
