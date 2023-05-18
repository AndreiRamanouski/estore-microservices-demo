package com.appdev.estore.order.orderservice.saga;

import com.appdev.estore.core.core.command.ReserveProductCommand;
import com.appdev.estore.core.core.event.ProductReservedEvent;
import com.appdev.estore.order.orderservice.command.event.OrderCreateEvent;
import javax.annotation.Nonnull;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
@NoArgsConstructor
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;

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
        //        process payment
    }

}
