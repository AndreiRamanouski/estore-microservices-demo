package com.appdev.estore.order.orderservice.data.query;

import com.appdev.estore.order.orderservice.command.model.response.OrderSummary;
import com.appdev.estore.order.orderservice.data.OrderEntity;
import com.appdev.estore.order.orderservice.data.repository.OrderRepository;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class OrderQueriesHandler {

    private final OrderRepository orderRepository;

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery query){
        OrderEntity byOrderId = orderRepository.findByOrderId(query.getOrderId());
        if(Objects.isNull(byOrderId)){
            throw new IllegalStateException("No order with id " + query.getOrderId());
        }
        return OrderSummary.builder()
                .orderId(byOrderId.getOrderId())
                .orderStatus(byOrderId.getOrderStatus())
                .reason(byOrderId.getReason())
                .build();
    }
}
