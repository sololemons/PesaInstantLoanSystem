package com.userservice.user.controller;

import com.userservice.security.dtos.UserDto;
import com.userservice.user.entities.User;
import com.userservice.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("get/user")
    public UserDto getUser(Authentication authentication) {
        String phoneNumber = authentication.getName();

        return userService.getUser(phoneNumber);
    }
    @PostMapping("/upload/profile/picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                       Authentication authentication) {
       String phoneNumber = authentication.getName();
        String filePath = userService.storeProfilePicture(file, phoneNumber);
        return ResponseEntity.ok(filePath);
    }
}

