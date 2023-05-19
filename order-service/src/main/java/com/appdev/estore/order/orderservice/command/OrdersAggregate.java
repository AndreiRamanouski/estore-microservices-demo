package com.appdev.estore.order.orderservice.command;

import com.appdev.estore.order.orderservice.command.commands.ApproveOrderCommand;
import com.appdev.estore.order.orderservice.command.commands.CreateOrderCommand;
import com.appdev.estore.order.orderservice.command.commands.RejectOrderCommand;
import com.appdev.estore.order.orderservice.command.event.OrderApprovedEvent;
import com.appdev.estore.order.orderservice.command.event.OrderCreateEvent;
import com.appdev.estore.order.orderservice.command.event.OrderRejectEvent;
import com.appdev.estore.order.orderservice.shared.OrderStatus;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
@Slf4j
public class OrdersAggregate {

    @AggregateIdentifier
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    @CommandHandler
    public OrdersAggregate(CreateOrderCommand command) {
        log.info("OrdersAggregate");
        if (command.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity cannot be less than 1");
        }

        OrderCreateEvent orderCreateEvent = new OrderCreateEvent();
        BeanUtils.copyProperties(command, orderCreateEvent);
        AggregateLifecycle.apply(orderCreateEvent);
    }

    @CommandHandler
    public void on(ApproveOrderCommand command) {
        log.info("on ApproveOrderCommand for order id {}", command.getOrderId());
        OrderApprovedEvent event = new OrderApprovedEvent(command.getOrderId());
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RejectOrderCommand command) {
        log.info("handle RejectOrderCommand");
        AggregateLifecycle.apply(
                OrderRejectEvent.builder().orderId(command.getOrderId()).reason(command.getReason()).build());
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent event) {
        log.info("on OrderApprovedEvent for order id {} and status {}", event.getOrderId(), event.getOrderStatus());
        this.orderStatus = event.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(OrderCreateEvent event) {
        log.info("Aggregate order with id {}", event.getOrderId());
        this.orderId = event.getOrderId();
        this.userId = event.getUserId();
        this.productId = event.getProductId();
        this.quantity = event.getQuantity();
        this.addressId = event.getAddressId();
        this.orderStatus = event.getOrderStatus();
    }


    @EventSourcingHandler
    public void on(OrderRejectEvent event) {
        log.info("on OrderRejectEvent");
        this.orderStatus = event.getOrderStatus();
    }
}
