package com.gongspot.project.domain.order.entity;

import com.gongspot.project.common.entity.BaseEntity;
import com.gongspot.project.domain.point.entity.Point;
import com.gongspot.project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_name")
    private String ordererName; // 주문자 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_method")
    private PayMethod payMethod; // 결제 방식

    @Column(length = 100, name = "merchant_uid")
    private String merchantUid; // 주문번호

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice; // 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "point_id")
    private Point point;

    @Builder(access = AccessLevel.PRIVATE)
    private Order(User user, Product product, String merchantUid, PayMethod payMethod, String ordererName, BigDecimal totalPrice) {
        this.user = user;
        this.product = product;
        this.merchantUid = merchantUid;
        this.payMethod = payMethod;
        this.ordererName = ordererName;
        this.totalPrice = totalPrice;
        this.paymentStatus = PaymentStatus.PENDING; // 초기 상태는 PENDING
    }

    public static Order createOrder(User user, Product product) {
        return Order.builder()
                .user(user)
                .product(product)
                .ordererName(user.getNickname())
                .totalPrice(product.getPrice())
                .build();
    }

    public void markAsPaid(String merchantUid, PayMethod payMethod) {
        this.merchantUid = merchantUid;
        this.payMethod = payMethod;
        this.paymentStatus = PaymentStatus.SUCCESS;
    }


    public void markAsCanceled() {
        this.paymentStatus = PaymentStatus.CANCELLED;
    }


    public void addPoint(Point point) {
        this.point = point;
    }
}
