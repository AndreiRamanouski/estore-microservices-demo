package com.appdev.estore.order.orderservice.command.event;

import com.appdev.estore.order.orderservice.shared.OrderStatus;
import lombok.Data;

@Data
public class OrderCreateEvent {

    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
