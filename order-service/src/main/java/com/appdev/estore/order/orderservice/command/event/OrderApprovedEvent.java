package com.appdev.estore.order.orderservice.command.event;

import com.appdev.estore.order.orderservice.shared.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {

    private String orderId;
    private OrderStatus orderStatus = OrderStatus.APPROVED;
}
