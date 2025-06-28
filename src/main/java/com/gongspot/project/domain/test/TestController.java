package com.gongspot.project.domain.test;

import com.gongspot.project.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/data")
    public ResponseEntity<ApiResponse<String>> success() {
        String data = testService.getSuccessData();
        return ResponseEntity.ok(ApiResponse.onSuccess(data));
    }

    @GetMapping("/error")
    public void error() {
        testService.throwError();
    }

}