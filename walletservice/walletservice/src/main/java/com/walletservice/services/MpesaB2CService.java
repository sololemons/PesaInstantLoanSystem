package com.walletservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletservice.entity.DisbursementRequest;
import com.walletservice.entity.TransactionType;
import com.walletservice.entity.Wallet;
import com.walletservice.entity.WalletTransactionHistory;
import com.walletservice.exceptions.UserNotFoundException;
import com.walletservice.repository.TransactionRepository;
import com.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MpesaB2CService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    private static final String CONSUMER_KEY = "lvckzKOnp2DMMcUjmmSL7FUrPHaTMqZXd9NVerZo3mljoaQJ";
    private static final String CONSUMER_SECRET = "cWVX4q8WJvGJJn4XG4GEGd0gLd5Z34K4bePjv03GdWZaJrrAFkWH15UyKDNHSTbI";
    private static final String ACCESS_TOKEN_URL = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
    private static final String B2C_URL = "https://sandbox.safaricom.co.ke/mpesa/b2c/v3/paymentrequest";
    private static final String SHORTCODE = "600998";
    private static final String INITIATOR_NAME = "testapi";
    private static final String SECURITY_CREDENTIAL = "BA++Or/qBIY9MnjzQUcXOO/EP4EELuPUpEz0vnIlYYCEfLnxpQsTfz6x4xpysNew60WibIVhbIYqwm5coAbuaPslhDKuMUvNh1aHfC1FNUR/ZGsmZGXHa1Cv7TnQQ9KeLgOT0fuSZBspfCL+r0muRj4j/FB5dHoBcrwE9L+730ikrZ/KxlpG+sWm4APiSJfnWN3Un0pBXgT/A3f1gIdeI/4r8iVGLtvQJHC6UmDQclKtCOLZeS/jj8VbVHs8d6rSI8dWK7AAM24Orn3onyVONtMBUo3DfzJsuVRL8FqOFPW4pGqmj1tKQ7N9fZpEdSQUIZ9spVKID+wl9AGn3dSo0Q==";
    private static final String RESULT_URL = "https://87ce-102-215-33-116.ngrok-free.app/mpesa/result";
    private static final String TIMEOUT_URL = "https://87ce-102-215-33-116.ngrok-free.app/mpesa/timeout";

    public ResponseEntity<String> sendB2CPayment(DisbursementRequest request) {
        try {
            String accessToken = getAccessToken();
            String originatorConversationID = UUID.randomUUID().toString();
            String formattedPhoneNumber = formatPhoneNumber(request.getPhoneNumber());
            String phoneNumber = request.getPhoneNumber();

            String payload = "{\n" +
                    "  \"OriginatorConversationID\": \"" + originatorConversationID + "\",\n" +
                    "  \"InitiatorName\": \"" + INITIATOR_NAME + "\",\n" +
                    "  \"SecurityCredential\": \"" + SECURITY_CREDENTIAL + "\",\n" +
                    "  \"CommandID\": \"BusinessPayment\",\n" +
                    "  \"Amount\": \"" + request.getAmount() + "\",\n" +
                    "  \"PartyA\": \"" + SHORTCODE + "\",\n" +
                    "  \"PartyB\": \"" +formattedPhoneNumber + "\",\n" +
                    "  \"Remarks\": \"Disbursement\",\n" +
                    "  \"QueueTimeOutURL\": \"" + TIMEOUT_URL + "\",\n" +
                    "  \"ResultURL\": \"" + RESULT_URL + "\",\n" +
                    "  \"Occasion\": \"Loan\"\n" +
                    "}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(B2C_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                updateWalletAfterDisbursement(request);
            }
            return response;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed: " + e.getMessage());
        }

    }

    private String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String auth = Base64.getEncoder().encodeToString((CONSUMER_KEY + ":" + CONSUMER_SECRET).getBytes());

        Request request = new Request.Builder()
                .url(ACCESS_TOKEN_URL)
                .get()
                .addHeader("Authorization", "Basic " + auth)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Failed to get access token: " + response);

            String body = response.body().string();
            JsonNode jsonNode = new ObjectMapper().readTree(body);
            return jsonNode.get("access_token").asText();
        }
    }
    private String formatPhoneNumber(String raw) {
        if (raw.startsWith("0")) {
            return "254" + raw.substring(1);
        }
        if (raw.startsWith("+")) {
            return raw.replace("+", "");
        }
        return raw;
    }
    private void updateWalletAfterDisbursement(DisbursementRequest request) {
        String phone = formatPhoneNumber(request.getPhoneNumber());

        Wallet wallet = walletRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new UserNotFoundException("Wallet not found for phone: " + phone));

        double currentBalance = Double.parseDouble(wallet.getWalletBalance());
        double disbursedAmount = Double.parseDouble(request.getAmount());
        wallet.setWalletBalance(String.valueOf(currentBalance - disbursedAmount));
        walletRepository.save(wallet);

        WalletTransactionHistory transaction = new WalletTransactionHistory();
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setTransactionAmount(disbursedAmount);
        transaction.setPhoneNumber(phone);
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setWallet(wallet);

        transactionRepository.save(transaction);
    }

}
