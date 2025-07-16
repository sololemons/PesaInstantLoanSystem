package com.applicationservice.services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OcrService {
    private static final Logger logger = LoggerFactory.getLogger(OcrService.class);


    public String extractText(File imageFile) {
        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tesseract.setLanguage("eng");

        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            logger.error(e.getMessage());
            return "";
        }
    }
}
