package com.appdev.estore.payment.command.handler;

import com.appdev.estore.core.core.event.PaymentProcessedEvent;
import com.appdev.estore.payment.query.entity.PaymentEntity;
import com.appdev.estore.payment.query.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventsHandler {

    private final PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        log.info("Save payment id {} and order id {}", event.getPaymentId(), event.getOrderId());
        paymentRepository.save(PaymentEntity.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .build());
    }
}
