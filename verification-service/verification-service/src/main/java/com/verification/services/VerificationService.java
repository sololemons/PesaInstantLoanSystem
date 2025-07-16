package com.verification.services;

import com.verification.configuration.RabbitMQConfig;
import com.verification.dtos.IdDetailsPayload;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.shared.dtos.VerifyDto;

@Service
@RequiredArgsConstructor

public class VerificationService {
    private static final Logger logger = LoggerFactory.getLogger(VerificationService.class);
    private final OcrService ocrService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.IMAGE_QUEUE)
    public void processAndSaveID(VerifyDto verifyDto) {
        try {

            long userId = verifyDto.getUserId();
            String fileName = verifyDto.getFileName();
            byte[] imageBytes = verifyDto.getImage();
            Path savePath = Paths.get("received_uploads", fileName);
            Files.createDirectories(savePath.getParent());

            Files.write(savePath, imageBytes);


            String extractedText = ocrService.extractText(savePath.toFile());
            logger.info("Extracted Text: {}", extractedText);

            String idNumber = parseIdNumber(extractedText);
            IdDetailsPayload idDetailsPayload = new IdDetailsPayload();
            idDetailsPayload.setIdNumber(idNumber);
            idDetailsPayload.setUserId(userId);
            logger.info("ID {}", idNumber);

           rabbitTemplate.convertAndSend(RabbitMQConfig.CRB_QUEUE, idDetailsPayload);
        } catch (Exception e) {
            logger.error("Error processing ID: ", e);
            throw new RuntimeException("Failed to process and save ID", e);
        }
    }

    private String parseIdNumber(String text) {
        Pattern pattern = Pattern.compile("\\b\\d{8}\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        int randomId = 10000000 + new java.util.Random().nextInt(90000000);
        return String.valueOf(randomId);
    }


    private String parseName(String text) {
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toLowerCase().contains("full name")) {
                if (i + 1 < lines.length) {
                    String nameLine = lines[i + 1].toLowerCase();
                    return nameLine.replaceAll("[^a-z\\s]", "").trim();
                }
            }
        }
        return "unknown";
    }



    private String parseGender(String text) {
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toLowerCase().contains("sex")) {
                if (i + 1 < lines.length) {
                    String genderLine = lines[i + 1].toLowerCase();
                    return genderLine.replaceAll("[^a-z]", "").trim();
                }
            }
        }
        return "unknown";
    }

    private String parseDateOfBirth(String text) {
        Pattern dobPattern = Pattern.compile("\\b\\d{2}\\.\\d{2}\\.\\d{4}\\b");
        Matcher matcher = dobPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "unknown";
    }


}
