package com.applicationservice.controllers;

import com.applicationservice.dtos.ApplicationDetails;
import com.applicationservice.dtos.ApplicationFormDto;
import com.applicationservice.dtos.UserIdDto;
import com.applicationservice.entities.ApplicationForm;
import com.applicationservice.retrofit.RetrofitService;
import com.applicationservice.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    private final RetrofitService retrofitService;
    @PostMapping("/loan")
    public ResponseEntity<?> createApplication(@ModelAttribute ApplicationDetails applicationDetails, @RequestHeader("Authorization") String autHeader) {
        if(applicationService.applyLoan(applicationDetails,autHeader)){
            return ResponseEntity.ok("Application sucessfull");
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/get/loan")
    public ResponseEntity<List<ApplicationFormDto>> getApplicationLoans(@RequestHeader("Authorization") String autHeader) {
        UserIdDto  userIdDto =retrofitService.getUserId(autHeader);
        long userId = userIdDto.getId();
        return ResponseEntity.ok(applicationService.getApplicationLoans(userId));
    }
    @GetMapping("get/loan/applicationId/{id}")
    public ResponseEntity<ApplicationFormDto> getApplicationId(@PathVariable int id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }
    @GetMapping("get/loan/status")
    public ResponseEntity<ApplicationFormDto> getApplicationStatus(@RequestParam String phoneNumber) {
        return ResponseEntity.ok(applicationService.getByProgressStatus(phoneNumber));
    }
}
