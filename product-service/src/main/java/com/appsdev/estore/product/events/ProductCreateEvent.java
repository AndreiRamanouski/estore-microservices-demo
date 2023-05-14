package com.appsdev.estore.product.events;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductCreateEvent {

    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
