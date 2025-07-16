package com.walletservice.controller;

import com.walletservice.dtos.WalletDto;
import com.walletservice.entity.DisbursementRequest;
import com.walletservice.entity.Wallet;
import com.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    @PostMapping("/deposit")
    public ResponseEntity<String> disburseFunds(@RequestBody DisbursementRequest request) {
        walletService.disburseFundsToWallet(request);
        return ResponseEntity.ok("Funds disbursed");
    }
    @GetMapping("/get/phoneNumber")
    public ResponseEntity<WalletDto> getPhoneNumber(@RequestParam String phoneNumber) {
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber;
        }
        System.out.println("received phone number : " + phoneNumber);
        return ResponseEntity.ok(walletService.getWalletByPhoneNumber(phoneNumber));

    }
}
