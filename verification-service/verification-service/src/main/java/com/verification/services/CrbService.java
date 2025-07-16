package com.verification.services;

import com.verification.configuration.RabbitMQConfig;
import com.verification.dtos.CrbResult;
import com.verification.dtos.IdDetailsPayload;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CrbService {
    private final RabbitTemplate rabbitTemplate;
    private final Logger logger = LoggerFactory.getLogger(CrbService.class);

    @RabbitListener(queues = RabbitMQConfig.CRB_QUEUE)
    private void verifyCrbService(IdDetailsPayload idDetailsPayload) {

        Random random = new Random();
        int age = 18 + random.nextInt(47);
        int noOfLoans = random.nextInt(10);
       // boolean blacklistedStatus = random.nextBoolean();
        boolean blacklistedStatus = false;
        int paidLoans = random.nextInt(noOfLoans + 1);
        int unpaidLoans = noOfLoans - paidLoans;
       // int unpaidLoansPastDue = unpaidLoans > 0 ? random.nextInt(unpaidLoans + 1) : 0;
        int unpaidLoansPastDue = 0;
        long userId = idDetailsPayload.getUserId();
        CrbResult crbResult = new CrbResult();
        crbResult.setAge(age);
        crbResult.setNoOfLoans(noOfLoans);
        crbResult.setCrbStatus(blacklistedStatus);
        crbResult.setNoOfPaidLoans(paidLoans);
        crbResult.setNoOfPaidUnpaidLoansPastDue(unpaidLoansPastDue);
        crbResult.setNoOfUnpaidLoans(unpaidLoans);
        crbResult.setUserId(userId);
        logger.info("Crb service crb result {}", crbResult);
        rabbitTemplate.convertAndSend(RabbitMQConfig.CREDIT_SCORING_QUEUE, crbResult);


    }


}
