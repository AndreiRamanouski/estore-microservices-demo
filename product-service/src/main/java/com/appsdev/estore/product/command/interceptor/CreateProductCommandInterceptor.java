package com.appsdev.estore.product.command.interceptor;

import com.appsdev.estore.product.command.CreateProductCommand;
import com.appsdev.estore.product.data.entity.ProductLookupEntity;
import com.appsdev.estore.product.data.repository.ProductLookupRepository;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @Nonnull List<? extends CommandMessage<?>> list) {
        return (index, command) -> {
            log.info("Intercepted command {}", command.getPayloadType());
            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

                ProductLookupEntity byProductIdOrTitle = productLookupRepository.findByProductIdOrTitle(
                        createProductCommand.getProductId(),
                        createProductCommand.getTitle());
                if (Objects.nonNull(byProductIdOrTitle)) {
                    log.info("Product already exists");
                    throw new IllegalStateException(
                            "Product already exists");
                } else {
                    log.info("Save a new product");
                }


            }
            return command;
        };
    }
}
