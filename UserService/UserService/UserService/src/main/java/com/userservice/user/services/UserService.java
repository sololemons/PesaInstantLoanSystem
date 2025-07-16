package com.userservice.user.services;

import com.userservice.security.dtos.UserDto;
import com.userservice.user.entities.User;
import com.userservice.user.exceptions.MissingFieldException;
import com.userservice.user.exceptions.UserNotFoundException;
import com.userservice.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final String uploadDir = "uploads/profile-pictures/";



    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDto getUser(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found with phone number: " + phoneNumber));

        UserDto userDto = new UserDto();
        userDto.setUserStatus(String.valueOf(user.getUserStatus()));
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setUserSignUpDate(String.valueOf(user.getUserSignupDate()));
        userDto.setProfilePathImage(user.getProfilePathImage());
        return userDto;
    }

    public String storeProfilePicture(MultipartFile file, String phoneNumber) {
        try {
            User user = userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new MissingFieldException("User not found"));

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            String relativePath = "uploads/profile-pictures/" + filename;

            user.setProfilePathImage(relativePath);
            userRepository.save(user);

            return user.getProfilePathImage();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
