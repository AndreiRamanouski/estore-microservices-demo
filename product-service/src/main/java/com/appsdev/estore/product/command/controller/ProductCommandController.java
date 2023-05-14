package com.appsdev.estore.product.command.controller;

import com.appsdev.estore.product.command.CreateProductCommand;
import com.appsdev.estore.product.command.model.CreateProductRestModel;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final CommandGateway commandGateway;
    private final Environment env;

    @GetMapping
    public List<String> getProducts() {
        log.info("getProducts");

        return List.of("hi", "hi2");
    }

    @PostMapping
    public String createProduct(@RequestBody CreateProductRestModel createProductRestModel) {
        log.info("createProduct");

        CreateProductCommand build = CreateProductCommand.builder()
                .title(createProductRestModel.getTitle())
                .quantity(createProductRestModel.getQuantity())
                .price(createProductRestModel.getPrice())
                .productId(UUID.randomUUID().toString()).build();
        log.info("Product to create {}", build);


        String returnValue;
        try {
            returnValue = commandGateway.sendAndWait(build);
        } catch (Exception exception){
            returnValue = exception.getLocalizedMessage();
        }
        log.info("returnValue is {}", returnValue);
        return returnValue;
    }

    @PutMapping
    public String updateProduct() {
        log.info("updateProduct");

        return "Update product";
    }

    @DeleteMapping
    public String deleteProduct() {
        log.info("deleteProduct");

        return "Delete product";
    }
}
