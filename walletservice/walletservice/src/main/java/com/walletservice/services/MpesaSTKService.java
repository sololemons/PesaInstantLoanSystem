package com.walletservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletservice.dtos.PaymentRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MpesaSTKService {

    private static final Logger logger = LoggerFactory.getLogger(MpesaSTKService.class);


    private static final String MPESA_ACCESS_TOKEN_URL ="https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
    private static final String CONSUMER_KEY = "lvckzKOnp2DMMcUjmmSL7FUrPHaTMqZXd9NVerZo3mljoaQJ";
    private static final String CONSUMER_SECRET = "cWVX4q8WJvGJJn4XG4GEGd0gLd5Z34K4bePjv03GdWZaJrrAFkWH15UyKDNHSTbI";
    private static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    private static final String SHORT_CODE = "174379";
    private static final String CALLBACK_URL = "https://87ce-102-215-33-116.ngrok-free.app/mpesa/callback";


    private static final String MPESA_STK_PUSH_URL =
            "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";

    public String processStkPush(PaymentRequest paymentRequest) throws IOException {
        String phoneNumber = paymentRequest.getPhoneNumber();
        String amount = paymentRequest.getAmount();

        OkHttpClient client = new OkHttpClient();
        String credentials = CONSUMER_KEY + ":" + CONSUMER_SECRET;
        String basicAuth = Base64.getEncoder().encodeToString(credentials.getBytes());

        Request tokenRequest = new Request.Builder()
                .url(MPESA_ACCESS_TOKEN_URL)
                .get()
                .addHeader("Authorization", "Basic " + basicAuth)
                .build();

        Response tokenResponse = client.newCall(tokenRequest).execute();
        if (!tokenResponse.isSuccessful()) {
            logger.error("Failed to retrieve access token: {}", tokenResponse);
            throw new IOException("Failed to retrieve access token");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(tokenResponse.body().string());
        String accessToken = jsonNode.get("access_token").asText();

        String timestamp = generateTimestamp();
        String password = Base64.getEncoder().encodeToString((SHORT_CODE + PASSKEY + timestamp).getBytes());

        String payload = String.format(
                "{\"BusinessShortCode\":\"%s\",\"Password\":\"%s\",\"Timestamp\":\"%s\"," +
                        "\"TransactionType\":\"CustomerPayBillOnline\",\"Amount\":\"%s\",\"PartyA\":\"%s\"," +
                        "\"PartyB\":\"%s\",\"PhoneNumber\":\"%s\",\"CallBackURL\":\"%s\",\"AccountReference\":\"TestPayment\"," +
                        "\"TransactionDesc\":\"STK Push Test\"}",
                SHORT_CODE, password, timestamp, amount, phoneNumber, SHORT_CODE, phoneNumber, CALLBACK_URL
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(MPESA_STK_PUSH_URL, entity, String.class);

        logger.info("STK Push request sent. Response: {}", response.getBody());

        return response.getBody();
    }

    private String generateTimestamp() {
        Date now = new Date();
        return String.format("%tY%<tm%<td%<tH%<tM%<tS", now);
    }
    public ResponseEntity<String> handleStkPushCallback(Map<String, Object> callback) {
        try {
            logger.info("✅ Raw M-Pesa STK Callback: {}", callback);

            Object body = callback.get("Body");
            logger.info(" Callback Body: {}", body);

            return ResponseEntity.ok("Callback received");
        } catch (Exception e) {
            logger.error(" Error handling callback: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Callback error");
        }
    }

}
