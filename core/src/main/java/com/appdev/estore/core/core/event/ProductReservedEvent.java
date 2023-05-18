package com.appdev.estore.core.core.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservedEvent {

    private String productId;
    private String orderId;
    private int quantity;
    private String userId;
}
