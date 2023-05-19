package com.appdev.estore.order.orderservice.command.controller;

import com.appdev.estore.order.orderservice.command.commands.CreateOrderCommand;
import com.appdev.estore.order.orderservice.command.model.request.OrderRequest;
import com.appdev.estore.order.orderservice.shared.OrderStatus;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@Slf4j
@RequiredArgsConstructor
public class OrderCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        log.info("createOrder");

        CreateOrderCommand build = CreateOrderCommand.builder().orderId(UUID.randomUUID().toString())
                .quantity(orderRequest.getQuantity())
                .productId(orderRequest.getProductId()).addressId(orderRequest.getAddressId())
                .orderStatus(OrderStatus.CREATED)
                //hardcoded for simplicity
                .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                .build();

        String returnValue;
        returnValue = commandGateway.sendAndWait(build);
        log.info("Returned value {}", returnValue);
        return returnValue;
    }

}
