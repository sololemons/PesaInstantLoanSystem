package com.walletservice.repository;

import com.walletservice.entity.WalletTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<WalletTransactionHistory, Long> {
    List<WalletTransactionHistory> findByPhoneNumber(String phoneNumber);
}
