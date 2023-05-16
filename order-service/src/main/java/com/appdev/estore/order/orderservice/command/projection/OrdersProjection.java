package com.appdev.estore.order.orderservice.command.projection;

import com.appdev.estore.order.orderservice.command.event.OrderCreateEvent;
import com.appdev.estore.order.orderservice.data.OrderEntity;
import com.appdev.estore.order.orderservice.data.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@ProcessingGroup("order-group")
@Component
public class OrdersProjection {

    private final OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreateEvent createEvent){
        log.info("Persist order with id {}", createEvent.getOrderId());
        try {

            OrderEntity saved = orderRepository.save(OrderEntity.builder()
                    .orderId(createEvent.getOrderId())
                    .addressId(createEvent.getAddressId())
                    .productId(createEvent.getProductId())
                    .userId(createEvent.getUserId())
                    .orderStatus(createEvent.getOrderStatus())
                    .quantity(createEvent.getQuantity())
                    .build());


            log.info("Order has been saved id: {}", saved.getOrderId());
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }

}
