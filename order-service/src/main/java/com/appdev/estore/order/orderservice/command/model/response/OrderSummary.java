package com.appdev.estore.order.orderservice.command.model.response;

import com.appdev.estore.order.orderservice.shared.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummary {

    private String orderId;
    private OrderStatus orderStatus;
    private String reason;
}
