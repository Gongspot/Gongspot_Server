package com.gongspot.project.domain.order.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import com.gongspot.project.domain.order.dto.OrderRequestDTO;
import com.gongspot.project.domain.order.entity.Order;
import com.gongspot.project.domain.order.entity.PayMethod;
import com.gongspot.project.domain.order.repository.OrderRepository;
import com.gongspot.project.domain.order.entity.Product;
import com.gongspot.project.domain.order.repository.ProductRepository;
import com.gongspot.project.domain.user.entity.User;
import com.gongspot.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 클라이언트의 결제 요청을 받아 서버 측에서 결제를 검증하고,
 * 주문 및 관련 엔티티의 상태를 업데이트합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PortoneApiService portoneApiService;

    /**
     * 클라이언트로부터 전달받은 결제 정보를 바탕으로 결제 완료를 처리합니다.
     * @param request 결제 정보를 담고 있는 DTO
     * @return 결제가 완료된 주문 엔티티
     */
    @Transactional
    public Order processPaymentDone(OrderRequestDTO request) {
        log.info("Processing payment for merchantUid: {}", request.getMerchantUid());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.PRODUCT_NOT_FOUND));

        // PayMethod가 null일 경우 CARD로 기본값 설정
        PayMethod payMethod = request.getPayMethod() != null ? request.getPayMethod() : PayMethod.CARD;

        Order order = Order.createOrder(user, product);

        portoneApiService.verifyPayment(request.getImpUid(), request.getTotalPrice());

        // 결제 성공 처리: Order 엔티티의 상태 업데이트
        order.markAsPaid(request.getMerchantUid(), payMethod);

        // 주문 엔티티 저장
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved successfully with ID: {}", savedOrder.getOrderId());

        return savedOrder;
    }
}
