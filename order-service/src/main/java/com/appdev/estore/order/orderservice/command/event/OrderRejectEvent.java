package com.appdev.estore.order.orderservice.command.event;

import com.appdev.estore.order.orderservice.shared.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRejectEvent {

    private String orderId;
    private String reason;
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.REJECTED;
}
