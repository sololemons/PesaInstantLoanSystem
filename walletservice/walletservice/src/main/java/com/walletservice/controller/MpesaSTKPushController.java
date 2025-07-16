package com.walletservice.controller;

import com.walletservice.dtos.PaymentRequest;
import com.walletservice.services.MpesaSTKService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/mpesa")
@RequiredArgsConstructor
public class MpesaSTKPushController {
    private final MpesaSTKService mpesaSTKService;

    @PostMapping("/stkpush")
    public ResponseEntity<String> makeStkPush(@RequestBody PaymentRequest paymentRequest) throws IOException {
         mpesaSTKService.processStkPush(paymentRequest);
         return ResponseEntity.ok().build();
    }
    @PostMapping("/callback")
    public ResponseEntity<String> mpesaCallback(@RequestBody Map<String, Object> callback) {
        return mpesaSTKService.handleStkPushCallback(callback);
    }

}
