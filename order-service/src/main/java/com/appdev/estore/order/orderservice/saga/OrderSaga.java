package com.appdev.estore.order.orderservice.saga;

import com.appdev.estore.core.core.command.ProcessPaymentCommand;
import com.appdev.estore.core.core.command.ReserveProductCommand;
import com.appdev.estore.core.core.data.FetchUserPaymentDetailsQuery;
import com.appdev.estore.core.core.event.PaymentProcessedEvent;
import com.appdev.estore.core.core.event.ProductReservedEvent;
import com.appdev.estore.core.core.user.User;
import com.appdev.estore.order.orderservice.command.ApproveOrderCommand;
import com.appdev.estore.order.orderservice.command.event.OrderApprovedEvent;
import com.appdev.estore.order.orderservice.command.event.OrderCreateEvent;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
@NoArgsConstructor
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreateEvent orderCreateEvent) {
        log.info("handle OrderCreateEvent");

        ReserveProductCommand command = ReserveProductCommand.builder()
                .orderId(orderCreateEvent.getOrderId())
                .productId(orderCreateEvent.getProductId())
                .quantity(orderCreateEvent.getQuantity())
                .userId(orderCreateEvent.getUserId())
                .build();
        log.info("Order event handled for orderId {} and productId {}", command.getOrderId(), command.getProductId());
        commandGateway.send(command, new CommandCallback<ReserveProductCommand, Object>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends ReserveProductCommand> commandMessage,
                    @Nonnull CommandResultMessage<?> commandResultMessage) {
                log.info("Callback {}", commandMessage.getCommandName());
                if (commandResultMessage.isExceptional()) {
                    //Start compensation
                }
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        log.info("Product reserved orderId {} productId {}", productReservedEvent.getOrderId(),
                productReservedEvent.getProductId());
        log.info("Fetch user details for the user {}", productReservedEvent.getUserId());
        //        process payment
        User user = null;
        try {

            FetchUserPaymentDetailsQuery query = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
            user = queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();

        } catch (Exception exception) {
            log.error("Exception {}", exception.getLocalizedMessage());
            //start compensating transaction

            return;
        }
        log.info("Found user with id {}, and card payment name {}", user.getUserId(),
                user.getPaymentDetails().getName());

        ProcessPaymentCommand command = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .paymentDetails(user.getPaymentDetails())
                .build();

        String paymentResult = null;
        try {
            log.info("Sent payment for verification");
            paymentResult = commandGateway.sendAndWait(command, 10, TimeUnit.SECONDS);
            log.info("Payment result {}", paymentResult);
        } catch (Exception exception) {
            log.error("Exception {}", exception.getLocalizedMessage());
            // start compensating transaction
        }
        if (Objects.isNull(paymentResult)) {
            log.info("The Process payment command returned an error, starting compensating transaction...");
        }

    }


    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent event) {
        log.info("handle PaymentProcessedEvent");
        ApproveOrderCommand command = ApproveOrderCommand.builder().orderId(event.getOrderId()).build();
        commandGateway.send(command);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent event) {
        log.info("handle OrderApprovedEvent");
        log.info("The order SAGA has been completed!!! Order id {} and status {}", event.getOrderId(),
                event.getOrderStatus());
        //add response entity
        //        SagaLifecycle.end(); the same as @EndSaga
    }

}
