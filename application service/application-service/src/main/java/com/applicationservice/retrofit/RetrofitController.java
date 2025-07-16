package com.applicationservice.retrofit;

import com.applicationservice.dtos.UserIdDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequiredArgsConstructor
@RequestMapping("application/retrofit")
public class RetrofitController {
    private final RetrofitService retrofitService;

    @PostMapping("/get/id")
    public UserIdDto getUserId(@RequestHeader ("Authorization") String autHeader) {
        return retrofitService.getUserId(autHeader);
    }

}
