package com.userservice.user.services;
import com.userservice.security.dtos.OptData;
import com.userservice.user.retrofit.AfricaTalkingApi;
import com.userservice.user.retrofit.RetrofitClient;
import com.userservice.user.utility.OtpStore;
import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final RetrofitClient retrofitClient;
    private final OtpStore otpStore;

    @Value("${africatalking.username}")
    private String username;

    @Value("${apikey}")
    private String apiKey;

    public String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    public boolean sendOtp(String phoneNumber, String otp) {
        AfricaTalkingApi api = retrofitClient.getClient().create(AfricaTalkingApi.class);
        String message = "Your OTP is: " + otp;


        try {
            Call<ResponseBody> call = api.sendSms(
                    apiKey.trim(),
                    username.trim(),
                    phoneNumber,
                    message
            );

            System.out.println("Calling URL: " + call.request().url());
            System.out.println("Sending OTP to: " + phoneNumber);
            System.out.println("Message: " + message);

            Response<ResponseBody> response = call.execute();

            if (response.isSuccessful()) {
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                System.out.println("OTP sent successfully. Response: " + responseBody);

                OptData otpData = new OptData(
                        otp,
                        phoneNumber,
                        System.currentTimeMillis(),
                        10 * 60 * 1000
                );
                otpStore.storeOtp(phoneNumber, otpData);
                System.out.println("Stored OTP in-memory for: " + phoneNumber);

                return true;
            } else {
                System.out.println("Failed to send OTP. Status Code: " + response.code());
                if (response.errorBody() != null) {
                    System.out.println("Error Body: " + response.errorBody().string());
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("Exception while sending OTP: " + e.getMessage());
            return false;
        }
    }
}