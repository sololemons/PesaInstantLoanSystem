package com.applicationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdDetails {
    private String idNumber;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String imagePath;
}
