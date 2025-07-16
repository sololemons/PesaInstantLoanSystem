package com.walletservice.controller;

import com.walletservice.entity.DisbursementRequest;
import com.walletservice.services.MpesaB2CService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mpesa")
@RequiredArgsConstructor
public class MpesaB2CController {

    private final MpesaB2CService service;
    private final Logger logger = LoggerFactory.getLogger(MpesaB2CController.class);



    @PostMapping("/b2c/withdraw")
    public ResponseEntity<String> disburse(@RequestBody DisbursementRequest request) {
        return service.sendB2CPayment(request);
    }

    @PostMapping("/result")
    public ResponseEntity<String> handleResult(@RequestBody String result) {
        logger.info("✅ B2C Result Callback Raw JSON:");
        logger.info(result);

        return ResponseEntity.ok("Result received");
    }


    @PostMapping("/timeout")
    public ResponseEntity<String> handleTimeout(@RequestBody String timeout) {
        System.out.println("⚠️ B2C Timeout Callback:\n" + timeout);
        return ResponseEntity.ok("Timeout received");
    }
}
