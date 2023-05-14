package com.appsdev.estore.product.query.handler;

import com.appsdev.estore.product.data.repository.ProductRepository;
import com.appsdev.estore.product.query.data.FindProductsQuery;
import com.appsdev.estore.product.query.model.ProductRestModel;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductsQueryHandler {

    private final ProductRepository productRepository;

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery findProductsQuery){

        return productRepository.findAll().stream()
                .map(entity -> ProductRestModel.builder().productId(entity.getProductId()).title(entity.getTitle())
                        .price(entity.getPrice()).quantity(entity.getQuantity()).build()).collect(Collectors.toList());

    }
}
