package com.appdev.estore.order.orderservice.error;

import javax.annotation.Nonnull;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

public class OrdersServiceEventsErrorHandler implements ListenerInvocationErrorHandler {

    @Override
    public void onError(@Nonnull Exception e, @Nonnull EventMessage<?> eventMessage,
            @Nonnull EventMessageHandler eventMessageHandler) throws Exception {
        throw e;
    }
}
