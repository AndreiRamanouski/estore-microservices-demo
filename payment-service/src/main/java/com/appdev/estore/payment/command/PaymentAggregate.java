package com.appdev.estore.payment.command;

import com.appdev.estore.core.core.command.ProcessPaymentCommand;
import com.appdev.estore.core.core.event.PaymentProcessedEvent;
import com.appdev.estore.core.core.payment.PaymentDetails;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
@NoArgsConstructor
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand command) {
        log.info("handle ProcessPaymentCommand");

        //todo validate the fields
        PaymentDetails paymentDetails = command.getPaymentDetails();
        {
            log.info("Validate payment details for {}", paymentDetails.getName());
            if (false) {
                throw new IllegalStateException("Invalid payment details");
            }
        }
        log.info("Payment accepted for order id {} and payment id {}", command.getOrderId(), command.getPaymentId());
        log.info("Publishing PaymentProcessedEvent");
        PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                .orderId(command.getOrderId())
                .paymentId(command.getPaymentId())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }


}
