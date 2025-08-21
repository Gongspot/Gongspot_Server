package com.gongspot.project.domain.order.repository;

import com.gongspot.project.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByMerchantUid(String merchantUid);
}
