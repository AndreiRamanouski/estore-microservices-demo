package com.appdev.estore.order.orderservice.data.query;

import lombok.Value;

@Value
public class FindOrderQuery {

    private final String orderId;
}
