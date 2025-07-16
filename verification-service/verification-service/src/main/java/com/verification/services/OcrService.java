package com.verification.services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OcrService {
private final static Logger log = LoggerFactory.getLogger(OcrService.class);
    public String extractText(File imageFile) {
        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tesseract.setLanguage("eng");


        try {
            String rawText = tesseract.doOCR(imageFile);
            return cleanExtractedText(rawText);
        } catch (TesseractException e) {
            log.error("Tesseract has fallen into an error {}",e.getMessage());
            return "";
        }
    }
    private String cleanExtractedText(String raw) {
        return raw
                .replaceAll("[^\\x00-\\x7F]", "")
                .replaceAll("[\\|_~`]", "")
                .replaceAll(" +", " ")
                .replaceAll("\\s*:\\s*", ": ")
                .trim();
    }
}
