package com.appdev.estore.order.orderservice.data.repository;

import com.appdev.estore.order.orderservice.data.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    OrderEntity findByOrderId(String orderId);
}
