package com.applicationservice.dtos;

import com.applicationservice.dtos.Occupation;
import com.applicationservice.dtos.Progress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationFormDto {
    private String fullName;
    private long idNumber;
    private String imagePath;
    private String dateOfBirth;
    private String userId;
    private Progress progress;
    private Occupation occupation;
    private LocalDate applicationDate;
    private long loanLimit;
    private String phoneNumber;
}