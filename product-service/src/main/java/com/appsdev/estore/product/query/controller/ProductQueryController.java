package com.appsdev.estore.product.query.controller;

import com.appsdev.estore.product.query.data.FindProductsQuery;
import com.appsdev.estore.product.query.model.ProductRestModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
@Slf4j
@RequiredArgsConstructor
public class ProductQueryController {

    private final QueryGateway queryGateway;

    @GetMapping
    public List<ProductRestModel> getProducts() {
        log.info("getProducts");
        return queryGateway.query(new FindProductsQuery(),
                ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
    }

}
