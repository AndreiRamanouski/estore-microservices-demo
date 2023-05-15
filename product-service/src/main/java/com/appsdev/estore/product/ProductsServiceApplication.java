package com.appsdev.estore.product;

import com.appsdev.estore.product.command.interceptor.CreateProductCommandInterceptor;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@RequiredArgsConstructor
public class ProductsServiceApplication {

    private final CreateProductCommandInterceptor commandInterceptor;
    private final CommandBus commandBus;

    public static void main(String[] args) {
        SpringApplication.run(ProductsServiceApplication.class, args);
    }

    @PostConstruct
    public void registerCreateProductCommandInterceptor() {
        commandBus.registerDispatchInterceptor(commandInterceptor);
    }
}
