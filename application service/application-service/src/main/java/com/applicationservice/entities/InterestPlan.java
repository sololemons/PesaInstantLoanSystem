package com.applicationservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InterestPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "principal")
    private double principal;
    @Column(name = "interest_rate")
    private double interestRate;
    @Column(name = "repayment_date")
    private LocalDate repaymentDate;
    @Column(name = "monthly_installments")
    private double monthlyInstallments;
    @Column(name = "total_payable")
    private double totalPayable;
    @OneToOne(mappedBy = "interestPlan")
    private ApplicationForm applicationForm;

}
