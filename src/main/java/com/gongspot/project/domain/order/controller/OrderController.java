package com.gongspot.project.domain.order.controller;

import com.gongspot.project.domain.order.dto.OrderRequestDTO;
import com.gongspot.project.domain.order.entity.Order;
import com.gongspot.project.domain.order.service.OrderService;
import com.gongspot.project.domain.order.service.PortoneApiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.siot.IamportRestClient.IamportClient;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final PortoneApiService portoneApiService;

    @PostMapping("/payment/validate")
    public String validatePayment(@RequestBody OrderRequestDTO request) {

        Order validatedOrder = orderService.processPaymentDone(request);

        log.info("결제 검증 및 주문 처리 성공. 주문 번호: {}", validatedOrder.getMerchantUid());

        return "결제 성공 및 서버 검증 완료";
    }
}