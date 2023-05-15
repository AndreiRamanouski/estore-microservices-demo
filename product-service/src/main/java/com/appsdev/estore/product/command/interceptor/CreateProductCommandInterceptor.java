package com.appsdev.estore.product.command.interceptor;

import com.appsdev.estore.product.command.CreateProductCommand;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @Nonnull List<? extends CommandMessage<?>> list) {
        return (index, command) -> {
            log.info("Intercepted command {}", command.getPayloadType());
            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
                if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    log.error("Price cannot be less or equal to zero");
                    throw new IllegalArgumentException("Price cannot be less or equal to zero");
                }
                if (Objects.isNull(createProductCommand.getTitle()) || createProductCommand.getTitle().isEmpty()) {
                    log.error("Title cannot be empty");
                    throw new IllegalArgumentException("Title cannot be empty");
                }

            }
            return command;
        };
    }
}
