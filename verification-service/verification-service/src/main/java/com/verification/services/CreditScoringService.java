package com.verification.services;

import com.verification.configuration.RabbitMQConfig;
import com.verification.dtos.CrbResult;
import com.shared.dtos.CreditResult;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditScoringService {

    private final RabbitTemplate rabbitTemplate;
    private final Logger logger = LoggerFactory.getLogger(CreditScoringService.class);



    @RabbitListener(queues = RabbitMQConfig.CREDIT_SCORING_QUEUE)
    public void creditScoringService(CrbResult crbResult) {

        int score = 1000;
        int totalLoans = crbResult.getNoOfLoans();
        int paidLoans = crbResult.getNoOfPaidLoans();
        int unpaidLoans = crbResult.getNoOfUnpaidLoans();
        int pastDue = crbResult.getNoOfPaidUnpaidLoansPastDue();
        boolean blacklisted = crbResult.isCrbStatus();
        int age = crbResult.getAge();

        char grade = 'D';

        if (blacklisted || pastDue > 0 || age < 21) {
            score = 300;
        } else {
            if (age < 23) {
                score -= 50;
            }

            if (totalLoans > 0) {
                double repaymentRatio = (double) paidLoans / totalLoans;
                if (repaymentRatio < 0.5) {
                    score -= 50;
                } else if (repaymentRatio < 0.75) {
                    score -= 30;
                }
            }

            score -= unpaidLoans * 10;
            score = Math.max(300, Math.min(score, 1000));

            if (score >= 800) grade = 'A';
            else if (score >= 700) grade = 'B';
            else if (score >= 600) grade = 'C';
        }

        long loanLimit = switch (grade) {
            case 'A' -> 25000;
            case 'B' -> 10000;
            case 'C' -> 5000;
            default -> 0;
        };


        CreditResult creditResult = new CreditResult();
        creditResult.setScore(score);
        creditResult.setLoanLimit(String.valueOf(loanLimit));
        creditResult.setCreditGrade(grade);
        creditResult.setUserId(crbResult.getUserId());
        logger.info("CreditResults: {}", creditResult);
        rabbitTemplate.convertAndSend(RabbitMQConfig.CREDIT_SCORING_EXCHANGE, "", creditResult);

    }
}
