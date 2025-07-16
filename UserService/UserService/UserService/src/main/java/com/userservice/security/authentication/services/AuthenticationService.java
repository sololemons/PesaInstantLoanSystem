package com.userservice.security.authentication.services;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.userservice.security.configuration.JwtService;
import com.userservice.security.dtos.AuthenticationRequest;
import com.userservice.security.dtos.AuthenticationResponse;
import com.userservice.security.dtos.RegisterRequest;
import com.userservice.security.dtos.ResetPasswordRequestDto;
import com.userservice.user.dtos.UserStatus;
import com.userservice.user.entities.User;
import com.userservice.user.exceptions.UserNotFoundException;
import com.userservice.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // Validate phone number before authentication
        String formattedPhoneNumber = validateAndFormatPhoneNumber(request.getPhoneNumber().trim());
        String Password = request.getPassword().trim();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        formattedPhoneNumber,
                        Password

                )
        );
        var user = repository.findByPhoneNumber(formattedPhoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found with phone number: " + formattedPhoneNumber));
        var jwtToken = jwtService.generateAuthenticationToken(user);



        return AuthenticationResponse.builder().
                token(jwtToken)
                .username(formattedPhoneNumber)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {

        // Validate phone number before Registering
        String formattedPhoneNumber = validateAndFormatPhoneNumber(request.getPhoneNumber());

        String password = request.getPassword().trim();
        String confirmPassword = request.getConfirmPassword().trim();


        if (!password.equals(confirmPassword)) {
            throw new UserNotFoundException("Passwords do not match!");
        }
        if (repository.findByPhoneNumber(formattedPhoneNumber).isPresent()) {
            throw new UserNotFoundException("Phone number is already registered!");
        }
        var user = User.builder()
                .phoneNumber(formattedPhoneNumber)
                .password(passwordEncoder.encode(request.getPassword()))
                .userSignupDate(LocalDate.now())
                .accountBalance(0.0)
                .userStatus(UserStatus.active)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateRegistrationToken(user);

        return AuthenticationResponse.builder().
                token(jwtToken)
                .build();

    }

    private String validateAndFormatPhoneNumber(String phoneNumber) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, "KE");
            if (!phoneUtil.isValidNumber(number)) {
                throw new UserNotFoundException("Invalid phone number!");
            }
            // Make sure that the number belongs to Kenya (+254)
            if (number.getCountryCode() != 254) {
                throw new UserNotFoundException("Only Kenyan phone numbers (+254) are allowed!");
            }


            return phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);

        } catch (NumberParseException e) {
            throw new UserNotFoundException("Invalid phone number format or it is null!");
        }
    }


    @Transactional
    public boolean resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        User user = repository.findByPhoneNumber(resetPasswordRequestDto.getPhoneNumber())
                .orElseThrow(() -> new UserNotFoundException("User with phone number not found"));

        String hashedPassword = passwordEncoder.encode(resetPasswordRequestDto.getNewPassword());
        user.setPassword(hashedPassword);

        repository.save(user);
        return true;
    }

}