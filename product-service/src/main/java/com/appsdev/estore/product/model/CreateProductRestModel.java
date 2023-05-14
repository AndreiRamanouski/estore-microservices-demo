package com.appsdev.estore.product.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRestModel {

    private String title;
    private BigDecimal price;
    private Integer quantity;
}
