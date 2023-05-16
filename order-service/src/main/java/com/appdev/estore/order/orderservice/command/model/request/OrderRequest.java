package com.appdev.estore.order.orderservice.command.model.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderRequest {

    @NotBlank
    private String productId;
    @Min(value = 1, message = "Quantity cannot be less than 1")
    private Integer quantity;
    @NotBlank
    private String addressId;
}
