package com.walletservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    @Column(name = "wallet_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long walletId;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "wallet_balance")
    private String walletBalance;
    @Column(name = "funds_due")
    private String fundsDue;
    @Column(name = "date_created")
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private List<WalletTransactionHistory> walletTransactionHistory;

}
