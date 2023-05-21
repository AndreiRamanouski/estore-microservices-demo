package com.appdev.estore.order.orderservice;

import com.appdev.estore.order.orderservice.error.OrdersServiceEventsErrorHandler;
import org.axonframework.config.Configuration;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import({ AxonXstreamConfig.class })
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler("order-group",
                conf -> new OrdersServiceEventsErrorHandler());
    }

    @Bean
    public DeadlineManager deadlineManager(Configuration configuration, SpringTransactionManager transactionManager){
        return SimpleDeadlineManager.builder()
                .scopeAwareProvider(new ConfigurationScopeAwareProvider(configuration))
                .transactionManager(transactionManager)
                .build();
    }
}
