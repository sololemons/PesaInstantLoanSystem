package com.userservice.user.entities;

import com.userservice.user.dtos.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;
    @Column(name = "phone_number",unique = true)
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Column(name = "user_signup_date")
    private LocalDate userSignupDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Column(name = "account_balance")
    private double accountBalance;
    @Column(name = "profile_path_image")
    private String profilePathImage;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

}
