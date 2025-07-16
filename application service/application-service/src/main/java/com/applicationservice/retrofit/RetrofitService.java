package com.applicationservice.retrofit;

import com.applicationservice.dtos.UserIdDto;
import com.applicationservice.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Service
public class RetrofitService {
    private static final Logger logger = LoggerFactory.getLogger(RetrofitService.class);
    private final ApiClient apiClient;

    public RetrofitService(RetrofitClient retrofitClient) {
        this.apiClient = retrofitClient.getClient().create(ApiClient.class);
    }


    public UserIdDto getUserId(String authHeader) {
        Call<UserIdDto> call = apiClient.getUserId(authHeader);
        try {
            Response<UserIdDto> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                logger.info("Successfully fetched {} UserID. Response: {}, Response code: {}",
                        response.body(), response.body(), response.code());
                return response.body();
            } else {
                logger.error("Failed to fetch UserId. Response code: {}, Error: {}",
                        response.code(), response.errorBody());
                throw new UserNotFoundException("Failed to fetch UserId. Response code: " + response.code());
            }
        } catch (IOException e) {
            logger.error("API call failed due to network error: {}", e.getMessage());
            throw new UserNotFoundException("API call failed due to network error");
        }
    }
}