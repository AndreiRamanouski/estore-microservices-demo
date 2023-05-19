package com.appdev.estore.order.orderservice.command.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ApproveOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;
}
