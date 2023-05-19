package com.appdev.estore.order.orderservice.command.projection;

import com.appdev.estore.order.orderservice.command.event.OrderApprovedEvent;
import com.appdev.estore.order.orderservice.command.event.OrderCreateEvent;
import com.appdev.estore.order.orderservice.data.OrderEntity;
import com.appdev.estore.order.orderservice.data.repository.OrderRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@ProcessingGroup("order-group")
@Component
public class OrdersEventHandler {

    private final OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreateEvent createEvent) {
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

    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        log.info("on OrderApprovedEvent");
        try {
            OrderEntity byOrderId = orderRepository.findByOrderId(orderApprovedEvent.getOrderId());
            if (Objects.isNull(byOrderId)) {
                log.error("Order id {} not found", orderApprovedEvent.getOrderId());
                throw new IllegalStateException("Order id not found");
            }
            byOrderId.setOrderStatus(orderApprovedEvent.getOrderStatus());
            orderRepository.save(byOrderId);
            log.info("Order {} has been updated to {}", orderApprovedEvent.getOrderId(),
                    orderApprovedEvent.getOrderStatus());
        } catch (Exception exception) {
            log.error("Exception {}", exception.getLocalizedMessage());
        }
    }

}
