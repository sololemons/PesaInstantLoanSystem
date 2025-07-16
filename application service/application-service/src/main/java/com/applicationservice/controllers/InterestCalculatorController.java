package com.applicationservice.controllers;

import com.applicationservice.dtos.PlanDto;
import com.applicationservice.dtos.InterestPlanDto;
import com.applicationservice.entities.InterestPlan;
import com.applicationservice.services.InterestCalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class InterestCalculatorController {
    private final InterestCalculateService interestCalculateService;
    @PostMapping("/calculate/interest")
    public ResponseEntity<PlanDto>createInterestPlan(@RequestBody InterestPlanDto interestPlanDto) {
        return ResponseEntity.ok(interestCalculateService.calculateInterestPlan(interestPlanDto));
    }
    @GetMapping("/get/interest/plan")
    public ResponseEntity<PlanDto> getInterestPlan(@RequestParam String phoneNumber) {
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber;
        }
        return ResponseEntity.ok(interestCalculateService.getInterestPlan(phoneNumber));
    }
}
