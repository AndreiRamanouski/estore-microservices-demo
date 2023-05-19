package com.appsdev.estore.product.command;

import com.appdev.estore.core.core.command.CancelProductReservationCommand;
import com.appdev.estore.core.core.command.ReserveProductCommand;
import com.appdev.estore.core.core.event.ProductReservationCanceledEvent;
import com.appdev.estore.core.core.event.ProductReservedEvent;
import com.appsdev.estore.product.command.events.ProductCreateEvent;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
@Slf4j
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) throws Exception {
        // Validate create product command
        if (command.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be less or equal to zero");
        }
        if (Objects.isNull(command.getTitle()) || command.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        ProductCreateEvent productCreateEvent = new ProductCreateEvent();

        BeanUtils.copyProperties(command, productCreateEvent);
        AggregateLifecycle.apply(productCreateEvent);
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand) {
        log.info("handle ReserveProductCommand");
        if (quantity < reserveProductCommand.getQuantity()) {
            throw new IllegalArgumentException("Not enough products in stock");
        }
        ProductReservedEvent reservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .userId(reserveProductCommand.getUserId())
                .quantity(reserveProductCommand.getQuantity())
                .productId(reserveProductCommand.getProductId())
                .build();

        AggregateLifecycle.apply(reservedEvent);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand command) {
        log.info("handle CancelProductReservationCommand for product id {}, reason {}", command.getProductId(),
                command.getReason());
        ProductReservationCanceledEvent event = ProductReservationCanceledEvent.builder()
                .orderId(command.getOrderId())
                .userId(command.getUserId())
                .productId(command.getProductId())
                .reason(command.getReason())
                .quantity(command.getQuantity())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductCreateEvent productCreateEvent) {
        log.info("Aggregate product with id {}", productCreateEvent.getProductId());
        this.productId = productCreateEvent.getProductId();
        this.price = productCreateEvent.getPrice();
        this.quantity = productCreateEvent.getQuantity();
        this.title = productCreateEvent.getTitle();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        log.info("on ProductReservedEvent");
        this.quantity -= productReservedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservationCanceledEvent event) {
        log.info("on ProductReservationCanceledEvent");
        this.quantity += event.getQuantity();
    }
}
