package com.userservice.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String phoneNumber;
    private String userSignUpDate;
    private String userStatus;
    private String profilePathImage;
}
