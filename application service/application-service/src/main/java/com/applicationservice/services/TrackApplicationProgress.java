package com.applicationservice.services;

import com.applicationservice.dtos.Progress;
import com.applicationservice.entities.ApplicationForm;
import com.applicationservice.exceptions.MissingFieldException;
import com.applicationservice.rabbitMQ.RabbitMQConfiguration;
import com.applicationservice.repositories.ApplicationRepository;
import com.shared.dtos.CreditResult;
import com.shared.dtos.ProgressDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TrackApplicationProgress {
    private final ApplicationRepository applicationRepository;
    private final Logger logger = LoggerFactory.getLogger(TrackApplicationProgress.class);


    @RabbitListener(queues =RabbitMQConfiguration.CREDIT_RESULT_QUEUE1)
    public void trackApplicationProgress(CreditResult creditResult) {
        logger.info("Track Application Progress{}",creditResult);
        char grade = creditResult.getCreditGrade();
        long userId = creditResult.getUserId();
        ApplicationForm applicationForm = applicationRepository
                .findByUserIdAndProgress(String.valueOf(userId), Progress.PENDING)
                .orElseThrow(() -> new MissingFieldException("No pending application found for userId: " + userId));

        if (grade == 'D') {

        applicationForm.setProgress(Progress.REJECTED);
        }
        else {
            applicationForm.setProgress(Progress.APPROVED);
            applicationForm.setLoanLimit(Long.parseLong(creditResult.getLoanLimit()));
        }
        applicationRepository.save(applicationForm);
    }
    @RabbitListener(queues =RabbitMQConfiguration.PROGRESS_QUEUE)
    public void updateProgress(ProgressDto progressDto) {
        String phoneNumber = progressDto.getPhoneNumber();
        Optional<ApplicationForm> applicationForm = applicationRepository.findByPhoneNumberAndProgress(phoneNumber,Progress.APPROVED);
        if (applicationForm.isPresent()) {
            ApplicationForm applicationForm1 = applicationForm.get();
            applicationForm1.setProgress(Progress.ACTIVE);
            applicationForm1.setLoanLimit(0L);
            applicationRepository.save(applicationForm1);
        }

    }

}
