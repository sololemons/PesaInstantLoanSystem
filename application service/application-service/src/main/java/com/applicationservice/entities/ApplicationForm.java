package com.applicationservice.entities;

import com.applicationservice.dtos.Occupation;
import com.applicationservice.dtos.Progress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application_details1")
public class ApplicationForm {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "id_number")
    private long idNumber;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "progress")
    @Enumerated(EnumType.STRING)
    private Progress progress;
    @Enumerated(EnumType.STRING)
    @Column(name = "occupation")
    private Occupation occupation;
    @Column(name = "applied_on")
    private LocalDate applicationDate;
    @Column(name = "loan_limit")
    private long loanLimit = 0;
    @Column(name = "phone_number")
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interest_id", referencedColumnName = "id")
    private InterestPlan interestPlan;
}
