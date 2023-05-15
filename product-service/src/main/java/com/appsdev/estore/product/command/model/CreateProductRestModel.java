package com.appsdev.estore.product.command.model;

import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRestModel {

    @NotBlank(message = "Product title is a required field")
    private String title;
    @Min(value = 1, message = "Value cannot be less than 1")
    @Max(value = 1000, message = "Value cannot be greater than 1000")
    private BigDecimal price;
    @Min(value = 1, message = "Quantity cannot be less than 1")
    @Max(value = 10, message = "Quantity cannot be greater than 10")
    private Integer quantity;
}
