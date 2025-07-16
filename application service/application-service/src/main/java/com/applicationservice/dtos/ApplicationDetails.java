package com.applicationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDetails {
    private String fullName;
    private MultipartFile image;
    private long idNumber;
    private String occupation;
    private String dateOfBirth;
}
