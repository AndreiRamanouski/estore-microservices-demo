package com.appdev.estore.order.orderservice.command.controller;

import com.appdev.estore.order.orderservice.command.commands.CreateOrderCommand;
import com.appdev.estore.order.orderservice.command.model.request.OrderRequest;
import com.appdev.estore.order.orderservice.command.model.response.OrderSummary;
import com.appdev.estore.order.orderservice.data.query.FindOrderQuery;
import com.appdev.estore.order.orderservice.shared.OrderStatus;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
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
    private final QueryGateway queryGateway;

    @PostMapping
    public OrderSummary createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        log.info("createOrder");

        String orderId = UUID.randomUUID().toString();
        CreateOrderCommand build = CreateOrderCommand.builder().orderId(orderId)
                .quantity(orderRequest.getQuantity())
                .productId(orderRequest.getProductId()).addressId(orderRequest.getAddressId())
                .orderStatus(OrderStatus.CREATED)
                //hardcoded for simplicity
                .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                .build();

        SubscriptionQueryResult<OrderSummary, OrderSummary> orderStatusOrderStatusSubscriptionQueryResult = queryGateway.subscriptionQuery(
                new FindOrderQuery(orderId), ResponseTypes.instanceOf(OrderSummary.class),
                ResponseTypes.instanceOf(OrderSummary.class));

        String returnValue;
        try {
            returnValue = commandGateway.sendAndWait(build);
            log.info("Returned value {}", returnValue);
            return orderStatusOrderStatusSubscriptionQueryResult.updates().blockFirst();
        } finally {
            orderStatusOrderStatusSubscriptionQueryResult.close();
        }
    }

}
