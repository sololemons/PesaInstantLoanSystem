package com.applicationservice.services;

import com.applicationservice.dtos.*;
import com.applicationservice.entities.ApplicationForm;
import com.applicationservice.exceptions.UserNotFoundException;
import com.applicationservice.rabbitMQ.RabbitMQConfiguration;
import com.applicationservice.repositories.ApplicationRepository;
import com.applicationservice.retrofit.RetrofitService;
import com.shared.dtos.VerifyDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ApplicationService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    private final ApplicationRepository applicationRepository;
    private final RetrofitService retrofitService;
    private final RabbitTemplate rabbitTemplate;


    @Transactional
    public boolean applyLoan(ApplicationDetails applicationDetails, String autHeader) {
        UserIdDto userIdDto = retrofitService.getUserId(autHeader);
        long userId = userIdDto.getId();
        String phoneNumber = userIdDto.getPhoneNumber();
        try {
            MultipartFile uploadedFile = applicationDetails.getImage();
            VerifyDto verifyDto = new VerifyDto();
            verifyDto.setFileName(uploadedFile.getOriginalFilename());
            verifyDto.setUserId(userId);
            byte[] imageBytes = uploadedFile.getBytes();
            verifyDto.setImage(imageBytes);

            rabbitTemplate.convertAndSend(RabbitMQConfiguration.IMAGE_QUEUE, verifyDto);

            ApplicationForm applicationForm = new ApplicationForm();
            applicationForm.setPhoneNumber(phoneNumber);
            applicationForm.setUserId(String.valueOf(userId));
            applicationForm.setApplicationDate(LocalDate.now());
            applicationForm.setImagePath(uploadedFile.getOriginalFilename());
            applicationForm.setFullName(applicationDetails.getFullName());
            applicationForm.setIdNumber(applicationDetails.getIdNumber());
            applicationForm.setDateOfBirth(applicationDetails.getDateOfBirth());
            applicationForm.setProgress(Progress.PENDING);
            applicationForm.setOccupation(Occupation.valueOf(applicationDetails.getOccupation()));
            applicationRepository.save(applicationForm);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }


        return true;
    }

    public List<ApplicationFormDto> getApplicationLoans(long userId) {
        List<ApplicationForm> forms = applicationRepository.findByUserId(String.valueOf(userId));
        return forms.stream().map(this::mapToDto).collect(Collectors.toList());
    }



    public ApplicationFormDto getApplicationById(int id) {
        ApplicationForm form = applicationRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Application not found with id: " + id));
        return mapToDto(form);
    }

    public ApplicationFormDto getByProgressStatus(String phoneNumber) {
        ApplicationForm form = applicationRepository
                .findByPhoneNumberAndProgress(phoneNumber, Progress.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("Application not found with status and phoneNumber: " + phoneNumber));
        return mapToDto(form);
    }

    private ApplicationFormDto mapToDto(ApplicationForm applicationForm) {
        ApplicationFormDto dto = new ApplicationFormDto();
        dto.setFullName(applicationForm.getFullName());
        dto.setLoanLimit(applicationForm.getLoanLimit());
        dto.setUserId(applicationForm.getUserId());
        dto.setProgress(applicationForm.getProgress());
        dto.setPhoneNumber(applicationForm.getPhoneNumber());
        dto.setProgress(applicationForm.getProgress());
        dto.setApplicationDate(applicationForm.getApplicationDate());
        return dto;
    }

}