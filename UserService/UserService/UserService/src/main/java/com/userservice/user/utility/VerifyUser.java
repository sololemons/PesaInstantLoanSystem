package com.userservice.user.utility;

import com.userservice.security.configuration.JwtService;
import com.userservice.user.dtos.IdDetails;
import com.userservice.user.entities.User;
import com.userservice.user.exceptions.UserNotFoundException;
import com.userservice.user.repositories.UserRepository;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.POST;

@RestController
@Data
@RequestMapping("/users/verify")
public class VerifyUser {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @PostMapping("/user")
    public IdDetails verifyUser(@RequestHeader ("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String phoneNumber = jwtService.extractUserName(token);
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        IdDetails details = new IdDetails();
        details.setId(user.getUserId());
        details.setPhoneNumber(user.getPhoneNumber());
        return details;


    }
}
