package com.appdev.estore.order.orderservice.command;

import com.appdev.estore.order.orderservice.command.event.OrderCreateEvent;
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
    public  String orderId;
    private  String userId;
    private  String productId;
    private  int quantity;
    private  String addressId;
    private  OrderStatus orderStatus;

    @CommandHandler
    public OrdersAggregate(CreateOrderCommand command){

        if (command.getQuantity() < 1){
            throw new IllegalArgumentException("Quantity cannot be less than 1");
        }

        OrderCreateEvent orderCreateEvent = new OrderCreateEvent();
        BeanUtils.copyProperties(command, orderCreateEvent);
        AggregateLifecycle.apply(orderCreateEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreateEvent event){
        log.info("Aggregate order with id {}", event.getOrderId());
        this.orderId = event.getOrderId();
        this.userId = event.getUserId();
        this.productId = event.getProductId();
        this.quantity = event.getQuantity();
        this.addressId  = event.getAddressId();
        this.orderStatus = event.getOrderStatus();
    }
}
