package com.walletservice.controller;

import com.walletservice.dtos.TransactionDto;
import com.walletservice.entity.WalletTransactionHistory;
import com.walletservice.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getWalletByPhoneNumber(@RequestParam String phoneNumber) {
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber;
        }
         return ResponseEntity.ok(transactionService.getTransactionsByPhoneNumber(phoneNumber));

    }
}
