package com.appsdev.estore.product.command.handler;

import com.appsdev.estore.product.command.events.ProductCreateEvent;
import com.appsdev.estore.product.data.entity.ProductLookupEntity;
import com.appsdev.estore.product.data.repository.ProductLookupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@RequiredArgsConstructor
@Slf4j
public class ProductLookupEventsHandler {

    private final ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreateEvent productCreateEvent) {
        log.info("Save in lookup table id: {} and title: {}", productCreateEvent.getProductId(), productCreateEvent.getTitle());
        productLookupRepository.save(ProductLookupEntity.builder()
                .productId(productCreateEvent.getProductId())
                .title(productCreateEvent.getTitle())
                .build());

    }

    @ResetHandler
    public void reset(){
        productLookupRepository.deleteAll();
    }

}
