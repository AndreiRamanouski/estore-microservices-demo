package com.appsdev.estore.product.query.handler;

import com.appdev.estore.core.core.event.ProductReservationCanceledEvent;
import com.appdev.estore.core.core.event.ProductReservedEvent;
import com.appsdev.estore.product.command.events.ProductCreateEvent;
import com.appsdev.estore.product.data.entity.ProductEntity;
import com.appsdev.estore.product.data.repository.ProductRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    @ExceptionHandler(resultType = Exception.class)
    private void handleException(Exception exception) throws Exception {
        log.error(exception.getMessage());
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    private void handleException(IllegalArgumentException exception) {
        log.error(exception.getMessage());
        throw exception;
    }

    @EventHandler
    public void on(ProductCreateEvent event) throws Exception {
        log.info("Persist product with id {}", event.getProductId());
        try {

            ProductEntity saved = productRepository.save(ProductEntity.builder()
                    .productId(event.getProductId())
                    .title(event.getTitle())
                    .price(event.getPrice())
                    .quantity(event.getQuantity())
                    .build());

            log.info("Product has been saved id: {}", saved.getProductId());
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent) {
        log.info("ProductReservedEvent is called for product with id {}", productReservedEvent.getProductId());
        getProductEntityAndUpdate(productReservedEvent.getProductId(),
                productReservedEvent.getQuantity(), false);

    }

    @EventHandler
    public void on(ProductReservationCanceledEvent event) {
        log.info("ProductReservationCanceledEvent is called for product with id {}", event.getProductId());
        getProductEntityAndUpdate(event.getProductId(),
                event.getQuantity(), true);
    }

    private void getProductEntityAndUpdate(String productId, int quantity, boolean cancel) {
        ProductEntity byProductId = productRepository.findByProductId(productId);
        if (Objects.isNull(byProductId)) {
            throw new IllegalStateException("Product does not exist");
        }
        if (cancel) {
            byProductId.setQuantity(byProductId.getQuantity() + quantity);
        } else {
            byProductId.setQuantity(byProductId.getQuantity() - quantity);
        }
        ProductEntity productEntity = productRepository.saveAndFlush(byProductId);
        log.info("Product quantity for {} updated to {}", productEntity.getTitle(), productEntity.getQuantity());
    }
}
