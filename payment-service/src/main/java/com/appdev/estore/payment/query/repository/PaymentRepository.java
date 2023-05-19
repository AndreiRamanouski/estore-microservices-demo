package com.appdev.estore.payment.query.repository;

import com.appdev.estore.payment.query.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

    PaymentEntity findByPaymentId(String paymentId);

    PaymentEntity findByPaymentIdOrOrderId(String paymentId, String orderId);
}
