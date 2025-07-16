package com.walletservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_history")
public class WalletTransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "transcation_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Column(name = "transaction_amount")
    private double transactionAmount;
    @Column(name = "transaction_code")
    private String transactionCode;
    @Column(name = "transaction_date")
    private LocalDateTime transactionDateTime;
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}
