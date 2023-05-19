package com.appdev.estore.order.orderservice.saga;

import com.appdev.estore.core.core.command.CancelProductReservationCommand;
import com.appdev.estore.core.core.command.ProcessPaymentCommand;
import com.appdev.estore.core.core.command.ReserveProductCommand;
import com.appdev.estore.core.core.data.FetchUserPaymentDetailsQuery;
import com.appdev.estore.core.core.event.PaymentProcessedEvent;
import com.appdev.estore.core.core.event.ProductReservationCanceledEvent;
import com.appdev.estore.core.core.event.ProductReservedEvent;
import com.appdev.estore.core.core.user.User;
import com.appdev.estore.order.orderservice.command.commands.ApproveOrderCommand;
import com.appdev.estore.order.orderservice.command.commands.RejectOrderCommand;
import com.appdev.estore.order.orderservice.command.event.OrderApprovedEvent;
import com.appdev.estore.order.orderservice.command.event.OrderCreateEvent;
import com.appdev.estore.order.orderservice.command.event.OrderRejectEvent;
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

            if (Objects.isNull(user) || Objects.isNull(user.getPaymentDetails())) {
                cancelProductReservation(productReservedEvent, "No payment details");
            }
        } catch (Exception exception) {
            log.error("Exception {}", exception.getLocalizedMessage());
            //start compensating transaction
            cancelProductReservation(productReservedEvent, exception.getLocalizedMessage());
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
            cancelProductReservation(productReservedEvent, exception.getLocalizedMessage());
            return;
        }
        if (!Objects.isNull(paymentResult)) {

            log.info("The Process payment command returned is null, starting compensating transaction...");
            cancelProductReservation(productReservedEvent, "Cannot process with provided details");
        }

    }

    private void cancelProductReservation(ProductReservedEvent event, String reason) {
        log.info("cancelProductReservation for product id {}", event.getProductId());
        commandGateway.send(CancelProductReservationCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .reason(reason)
                .build());
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
        log.info("The order SAGA has been completed!!! The order is approved. Order id {} and status {}",
                event.getOrderId(),
                event.getOrderStatus());
        //add response entity
        //        SagaLifecycle.end(); the same as @EndSaga
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCanceledEvent event) {
        log.info("handle ProductReservationCanceledEvent order id {} reason {}", event.getOrderId(), event.getReason());
        commandGateway.send(RejectOrderCommand.builder().orderId(event.getOrderId()).reason(event.getReason()).build());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectEvent event) {
        log.info("handle OrderRejectEvent");
        log.info("The order saga has been completed!!! The order has been canceled. Order id {}, reason {}",
                event.getOrderId(), event.getReason());

    }

}
