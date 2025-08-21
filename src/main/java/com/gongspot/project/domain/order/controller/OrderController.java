package com.gongspot.project.domain.order.controller;

import com.gongspot.project.domain.order.dto.OrderRequestDTO;
import com.gongspot.project.domain.order.service.OrderService;
import com.gongspot.project.domain.order.service.PortoneApiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import java.io.IOException;


@RestController
@RequestMapping("order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final PortoneApiService portoneApiService;
    private IamportClient iamportClient;

    @Value("${imp.api-key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    @PostMapping("/payment/validate")
    public String validatePayment(@RequestBody OrderRequestDTO request) {

        portoneApiService.verifyPayment(request.getImpUid(), request.getTotalPrice());

        orderService.processPaymentDone(request);

        log.info("결제 검증 및 주문 처리 성공. 주문 번호: {}", request.getMerchantUid());

        return "결제 성공 및 서버 검증 완료";
    }
}