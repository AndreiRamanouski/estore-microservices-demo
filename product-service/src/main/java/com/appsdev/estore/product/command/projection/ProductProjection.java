package com.appsdev.estore.product.command.projection;

import com.appsdev.estore.product.command.events.ProductCreateEvent;
import com.appsdev.estore.product.data.entity.ProductEntity;
import com.appsdev.estore.product.data.repository.ProductRepository;
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
public class ProductProjection {

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
}
