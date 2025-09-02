package com.gongspot.project.domain.order.dto;

import com.gongspot.project.domain.order.entity.PayMethod;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderRequestDTO {

    private String impUid;
    private Long userId;
    private Long productId;
    private PayMethod payMethod;
    private Long totalPrice;
    private String merchantUid;

}
